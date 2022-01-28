package IR;

import TEMP.TEMP;

import java.util.*;

public class Register_Allocation {
    Map<IR_Node, List<IR_Node>> cfg;
    Map<String, List<IR_Node>> labelWereCalled;
    Map<String, IR_Node> existingLabels;
    IR_Node last;

    Map<IR_Node, Set<Integer>> in;
    Map<IR_Node, Set<Integer>> out;
    Set<IR_Node> visited;
    boolean hasChanged = true;

    Set<Interference_Node> allRegisters;

    Map<Interference_Node, Set<Interference_Node>> interferenceGraph;
    Stack<Interference_Node> stack;
    Map<Integer, Integer> irRegisterToPhysical;


    public void addCommandToCFG(IR_Node node) {
        for (int register : node.willLive) {
            allRegisters.add(new Interference_Node(register));
        }
        if (node.willDie != -1)
            allRegisters.add(new Interference_Node(node.willDie));

        if (last != null) {
            cfg.putIfAbsent(node, new ArrayList<>());
            if (!last.isJump) {
                cfg.get(node).add(last);
                last.children.add(node);
            }
            if (node.isLabel) {
                existingLabels.putIfAbsent(node.labelName, node);
                if (labelWereCalled.get(node.labelName) != null){
                    for (IR_Node nodeCalledLabel: labelWereCalled.get(node.labelName)) {
                        cfg.get(node).add(nodeCalledLabel);
                        nodeCalledLabel.children.add(node);
                    }
                }
            }
            if (node.isJump || node.isBranch) {
                if (existingLabels.containsKey(node.labelName)) {
                    IR_Node labelNode = existingLabels.get(node.labelName);
                    cfg.get(labelNode).add(node);
                    node.children.add(labelNode);
                } else {
                    labelWereCalled.putIfAbsent(node.labelName, new ArrayList<>());
                    labelWereCalled.get(node.labelName).add(node);
                }
            }
        } else {
            cfg.put(node, new ArrayList<>());
        }
        last = node;
    }

    public void livenessAnalysis() {
        while (hasChanged) {
            hasChanged = false;
            for (IR_Node node : cfg.keySet()) {
                if(!visited.contains(node)) {
                    dfs(node);
                }
            }
            visited.clear();
        }
    }

    public void dfs(IR_Node node) {
        visited.add(node);
        int prevInSize = in.getOrDefault(node, new HashSet<>()).size();
        int prevOutSize = out.getOrDefault(node, new HashSet<>()).size();
        in.put(node, new HashSet<>());
        out.put(node, new HashSet<>());

        for (IR_Node child : node.children){
            in.putIfAbsent(child, new HashSet<>());
            out.get(node).addAll(in.get(child));
        }

        in.get(node).addAll(out.get(node));
        if (node.willDie != -1)
            in.get(node).remove(node.willDie);
        in.get(node).addAll(node.willLive);

        if (!hasChanged && (out.get(node).size() != prevOutSize || in.get(node).size() != prevInSize))
            hasChanged = true;


        for (IR_Node parent: cfg.get(node)) {
            if (visited.contains(parent)) continue;
            dfs(parent);
        }
    }

    private Interference_Node getRegisterNode(int register) {
        for (Interference_Node node : allRegisters){
            if (node.number == register)
                return node;
        }
        return null;
    }

    private void buildGraphFromOutOrIn(Collection<Set<Integer>> group) {
        for (Set<Integer> inRegisters: group) {
            for (int register1 : inRegisters) {
                for (int register2 : inRegisters) {
                    if (register1 != register2) {
                        interferenceGraph.get(getRegisterNode(register1))
                            .add(getRegisterNode(register2));
                    }
                }
            }
        }
    }

    public void constructInterferenceGraph() {
        interferenceGraph = new HashMap<>();
        for (Interference_Node register : allRegisters) { // initializing all registers to the graph without neighbors
            interferenceGraph.put(register, new HashSet<>());
        }
        buildGraphFromOutOrIn(in.values());
        buildGraphFromOutOrIn(out.values());
    }

    private int sizeOfNeighborsNotInStack(Interference_Node node) {
        Set<Interference_Node> neighbors = interferenceGraph.get(node);
        int sum = 0;
        for (Interference_Node neighbor : neighbors) {
            if (!neighbor.inStack) {
                sum++;
            }
        }
        return sum;
    }

    public int getMaxColorAvailable(Set<Interference_Node> nodes) {
        int maxColor = -1;
        for (Interference_Node node : nodes) {
            maxColor = Math.max(maxColor, node.color);
        }
        return maxColor + 1;
    }

    public void coloringAlgorithm() {
        while (stack.size() != allRegisters.size()) {
            for (Interference_Node node : interferenceGraph.keySet()) {
                if (sizeOfNeighborsNotInStack(node) < 10) {
                    node.inStack = true;
                    stack.push(node);
                }
            }
        }

        // pop from the stack
        while (!stack.isEmpty()) {
            Interference_Node node = stack.pop();
            node.color = getMaxColorAvailable(interferenceGraph.get(node));
            irRegisterToPhysical.put(node.number, node.color);
            node.inStack = false;
        }
    }

    public void chaitinAlgorithm() {
        livenessAnalysis();
        constructInterferenceGraph();
        coloringAlgorithm();
    }
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static Register_Allocation instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/

    protected Register_Allocation() {
        cfg = new HashMap<>();
        labelWereCalled = new HashMap<>();
        existingLabels = new HashMap<>();
        visited = new HashSet<>();
        in = new HashMap<>();
        out = new HashMap<>();
        stack = new Stack<>();
        allRegisters = new HashSet<>();
        irRegisterToPhysical = new HashMap<>();
    }

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/

    public static Register_Allocation getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new Register_Allocation();
        }
        return instance;
    }

    public static void register_allocation_algorithm()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new Register_Allocation();
        }
        instance.chaitinAlgorithm();
    }

    public static Map<Integer, Integer> getIrRegisterToPhysical()
    {
        return instance.irRegisterToPhysical;
    }


/*
    public static void main(String [] args) {
        Register_Allocation r = new Register_Allocation();
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 1));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 2));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 3));
        r.addCommandToCFG(new IR_Node(Arrays.asList(2, 3), 4));
        r.addCommandToCFG(new IR_Node(Arrays.asList(1, 4), 5));
        r.addCommandToCFG(new IR_Node(Arrays.asList(5), -1));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 6));
        r.addCommandToCFG(new IR_Node(Arrays.asList(6), -1, false, true, false, "end"));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 7));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 8));
        r.addCommandToCFG(new IR_Node(Arrays.asList(7, 8), 9));
        r.addCommandToCFG(new IR_Node(Arrays.asList(9), -1));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), -1, false, false, true, "end"));
        r.addCommandToCFG(new IR_Node(new ArrayList<>(), 10));
        r.addCommandToCFG(new IR_Node(Arrays.asList(10), -1));
        Map<Integer, Integer> res = r.chaitinAlgorithm();
    }
*/
}
