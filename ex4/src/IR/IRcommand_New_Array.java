package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_New_Array extends IRcommand
{
    public TEMP exp_temp;
    public TEMP dst;

    public IRcommand_New_Array(TEMP dst, TEMP exp_temp)
    {
        //dst = NEW type [exp_temp]
        this.exp_temp = exp_temp;
        this. dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Arrays.asList(exp_temp.getSerialNumber(), dst.getSerialNumber()),
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
