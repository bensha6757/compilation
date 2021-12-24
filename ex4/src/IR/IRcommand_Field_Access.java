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

public class IRcommand_Field_Access extends IRcommand
{
    TEMP dst;
    TEMP var_temp;
    String fieldName;

    public IRcommand_Field_Access(TEMP dst, TEMP var_temp,String fieldName)
    {
        this.dst = dst;
        this.var_temp = var_temp;
        this.fieldName = fieldName;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().field_access(dst, var_temp, fieldName);
    }
}
