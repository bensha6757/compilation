package IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IR_Node {
    static int global_id = 0;
    int id;
    List<Integer> willLive;
    int willDie;
    boolean isJump;
    boolean isBranch;
    boolean isLabel;
    String labelName;
    List<IR_Node> children;

    public IR_Node(List<Integer> willLive, int willDie) {
        this(willLive, willDie, false, false, false, null);
    }

    public IR_Node(List<Integer> willLive, int willDie, boolean isJump, boolean isBranch, boolean isLabel, String labelName) {
        id = global_id++;
        this.willLive = willLive;
        this.willDie = willDie;
        this.isJump = isJump;
        this.isBranch = isBranch;
        this.isLabel = isLabel;
        this.labelName = labelName;
        this.children = new ArrayList<>();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IR_Node ir_node = (IR_Node) o;
        return id == ir_node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
