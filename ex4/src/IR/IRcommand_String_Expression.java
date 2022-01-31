package IR;

import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_String_Expression extends IRcommand{

    String str;
    TEMP dst;
    public IRcommand_String_Expression(String str, TEMP dst) {
        this.str = str;
        this.dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }
    @Override
    public void MIPSme() {

    }
}
