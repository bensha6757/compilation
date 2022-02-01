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

public class IRcommand_PrintInt extends IRcommand
{
	TEMP t;
	
	public IRcommand_PrintInt(TEMP t)
	{
		this.t = t;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), t.getSerialNumber()));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_int(t);
	}
}
