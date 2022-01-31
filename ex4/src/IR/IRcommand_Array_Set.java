package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_Array_Set extends IRcommand
{
    public TEMP dst;
    public TEMP t;

    public IRcommand_Array_Set(TEMP dst, TEMP t)
    {
        // dst (t1[t2]) = t3
        this.dst = dst;
        this.t = t;

        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Collections.singletonList(t.getSerialNumber()),
                        dst.getSerialNumber()));
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_set(dst, t);
    }
}
