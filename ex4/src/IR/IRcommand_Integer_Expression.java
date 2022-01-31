package IR;

import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Integer_Expression extends IRcommand{

    int value;
    TEMP dst;
    public IRcommand_Integer_Expression(TEMP dst, int value) {
        this.dst = dst;
        this.value = value;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }
    @Override
    public void MIPSme() {

    }
}
