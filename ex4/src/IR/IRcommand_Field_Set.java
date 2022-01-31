package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_Field_Set extends IRcommand
{
    public TEMP t1;
    public String fieldName;
    public TEMP t2;

    public IRcommand_Field_Set(TEMP t1, String fieldName, TEMP t2)
    {
        this.t1 = t1;
        this.fieldName = fieldName;
        this.t2 = t2;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(
            Collections.singletonList(t2.getSerialNumber()), t1.getSerialNumber()));
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_set(t1,fieldName,t2);
    }
}
