/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import java.util.*;

/*******************/
class Tuple {
    public String id;
    public Integer offset;
    public Tuple(String id, Integer offset) {
        this.id = id;
        this.offset = offset;
    }
}

public class IR
{
	private IRcommand head = null;
	private IRcommandList tail = null;

    private List<Tuple> localVariableToOffset = new ArrayList<>();
    private int localVarPointer = -44;

    private Map<String, Integer> argumentsToOffset = new HashMap<>();
    private int argsPointer = 8;

    private Stack<Integer> previousLocalsSize = new Stack<>();

    private String currFuncName = null;

    private List<String> globals = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    private List<IRcommand> globalToInitializeCommands = new ArrayList<>();


    /******************/
	/* Add IR command */
	/******************/

    public void beginScope()
    {
        previousLocalsSize.push(localVariableToOffset.size());
    }

    public void endScope()
    {
        int prevSize = previousLocalsSize.pop();
        while (localVariableToOffset.size() > prevSize) {
            this.localVariableToOffset.remove(this.localVariableToOffset.size() - 1);
            this.localVarPointer += 4;
        }
    }

    public void enterLocalVarToStack(String id) {
        localVariableToOffset.add(new Tuple(id, localVarPointer));
		localVarPointer -= 4;
    }

    public boolean isLocalVarExists(String id) {
        return getVarOffset(id) != -1 || argumentsToOffset.containsKey(id);
    }


    public void enterArgToStack(String id) {
        argumentsToOffset.putIfAbsent(id, argsPointer);
        argsPointer += 4;
    }

    public int getVarOffset(String id) {
        for (int i = localVariableToOffset.size() - 1 ; i >= 0 ; i--) {
            if (localVariableToOffset.get(i).id.equals(id))
                return localVariableToOffset.get(i).offset;
        }
        return -1;
    }
    public int getVariableOffset(String id) {
        int varOffset = getVarOffset(id);
        if (varOffset != -1)
            return varOffset;
        return argumentsToOffset.get(id);
    }

    public void clearStack() {
        localVariableToOffset = new ArrayList<>();
        argumentsToOffset.clear();
        localVarPointer = -44;
        argsPointer = 8;
    }

    public List<String> getGlobals() {
        return globals;
    }

    public void addGlobal(String s)
    {
        globals.add(s);
    }

    public List<String> getStrings() {
        return strings;
    }

    public int addString(String s)
    {
        strings.add(s);
        return strings.size() - 1;
    }


    public void setCurrentFunction(String funcName){
        this.currFuncName = funcName;
    }

    public String getCurrentFunction(){
        return this.currFuncName;
    }


    public void Add_Global_Command(IRcommand cmd) {
        globalToInitializeCommands.add(cmd);
    }

    public List<IRcommand> getGlobalToInitializeCommands() {
        return globalToInitializeCommands;
    }

    public void Add_IRcommand(IRcommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}
	}




	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
}
