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

public class IRcommand_Array_Access extends IRcommand

{
    TEMP dst;
    TEMP var;
    TEMP subscript;

    public IRcommand_Array_Access(TEMP dst, TEMP var,TEMP subscript)
    {
        this.var = var;
        this.subscript = subscript;
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().array_access(dst, var, subscript);
    }
}
