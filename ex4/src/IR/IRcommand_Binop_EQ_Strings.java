package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;

public class IRcommand_Binop_EQ_Strings extends IRcommand{

    TEMP dst;
    TEMP t1;
    TEMP t2;
    public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;

        Register_Allocation.getInstance().addCommandToCFG(
            new IR_Node(Arrays.asList(t1.getSerialNumber(), t2.getSerialNumber()),
                dst.getSerialNumber()));

    }

    public void MIPSme()
    {
        MIPSGenerator.getInstance().eqStrings(dst, t1, t2);
    }


}
