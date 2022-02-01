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
import java.util.Collections;

public class IRcommand_New_Class extends IRcommand
{
    TEMP var_type;
    String type_name;

    public IRcommand_New_Class(TEMP var_type, String type_name)
    {
        //newExp -> NEW type
        this.var_type = var_type;
        this.type_name = type_name;

        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Collections.emptyList(),
                        var_type.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().allocateNewClass(var_type, "allocate_new_instance_for_class_" + this.type_name);
    }
}
