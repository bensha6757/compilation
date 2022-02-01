/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

import java.util.Collections;

public class IRcommand_Field_Access extends IRcommand
{
    TEMP dst;
    TEMP var_temp;
    int fieldOffset;

    public IRcommand_Field_Access(TEMP dst, TEMP var_temp, int fieldOffset)
    {
        this.dst = dst;
        this.var_temp = var_temp;
        this.fieldOffset = fieldOffset;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(
            Collections.singletonList(this.var_temp.getSerialNumber()), this.dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().fieldAccess(dst, var_temp, fieldOffset);
    }
}
