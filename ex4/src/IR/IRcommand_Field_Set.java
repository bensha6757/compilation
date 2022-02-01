package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_Field_Set extends IRcommand
{
    public TEMP t1;
    public TEMP t2;

    public IRcommand_Field_Set(TEMP t1, TEMP t2)
    {
        this.t1 = t1;
        this.t2 = t2;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(
            Collections.singletonList(t2.getSerialNumber()), t1.getSerialNumber()));
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().fieldSet(t1, t2);
    }
}
