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

public class IRcommand_Return extends IRcommand
{
    TEMP exp;
    public IRcommand_Return(TEMP exp)
    {
        this.exp = exp;
        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.singletonList(exp.getSerialNumber()), -1));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().returnFunc(exp);
    }
}
