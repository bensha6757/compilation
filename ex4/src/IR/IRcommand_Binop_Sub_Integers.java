package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;

public class IRcommand_Binop_Sub_Integers extends IRcommand{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Sub_Integers(TEMP dst,TEMP t1,TEMP t2)
    {
        //dst = t1 - t2
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Arrays.asList(t1.getSerialNumber(), t2.getSerialNumber()),
                        dst.getSerialNumber()));
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().sub(dst,t1,t2);
    }

}
