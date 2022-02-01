package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_New_Array extends IRcommand
{
    public TEMP exp_temp;
    public TEMP dst;

    public IRcommand_New_Array(TEMP exp_temp, TEMP dst)
    {
        //dst = NEW type [exp_temp]
        this.exp_temp = exp_temp;
        this. dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Collections.singletonList(exp_temp.getSerialNumber()),
                        dst.getSerialNumber()));

    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().arrayAllocate(dst, exp_temp);
    }
}
