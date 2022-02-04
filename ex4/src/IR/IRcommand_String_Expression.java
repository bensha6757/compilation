package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_String_Expression extends IRcommand{

    int strIdx;
    TEMP dst;
    public IRcommand_String_Expression(int strIdx, TEMP dst) {
        this.strIdx = strIdx;
        this.dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }
    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().stringExpression(dst, strIdx);
    }
}
