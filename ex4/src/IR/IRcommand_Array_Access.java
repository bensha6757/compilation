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

import java.util.Arrays;

public class IRcommand_Array_Access extends IRcommand

{
    TEMP dst;
    TEMP var;
    TEMP subscript;

    public IRcommand_Array_Access(TEMP dst, TEMP var,TEMP subscript)
    {
        //dst = var[subscript]
        this.var = var;
        this.subscript = subscript;
        this.dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Arrays.asList(var.getSerialNumber(), subscript.getSerialNumber()),
                        dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().array_access(dst, var, subscript);
    }
}
