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

public class IRcommand_Return extends IRcommand
{
    TEMP exp_temp;
    public IRcommand_Return()
    {

    }
    /*
    public IRcommand_Return(TEMP exp_temp)
    {
        this.exp_temp = exp_temp;
    }
    */
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().returnFunc();
    }
}
