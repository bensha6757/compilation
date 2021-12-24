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

public class IRcommand_New_Class extends IRcommand
{
    TEMP var_type;
    String type_name;

    public IRcommand_New_Class(TEMP var_type, String type_name)
    {
        this.var_type = var_type;
        this.type_name = type_name
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().new_class(var_type, type_name);
    }
}
