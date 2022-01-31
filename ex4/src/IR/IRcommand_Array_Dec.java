package IR;

import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Array_Dec extends IRcommand{

    TEMP dst;
    String typeName;
    public IRcommand_Array_Dec(TEMP dst, String typeName) {
        this.dst = dst;
        this.typeName = typeName;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }

    @Override
    public void MIPSme() {

    }
}
