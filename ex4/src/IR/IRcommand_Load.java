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
import java.util.Collection;
import java.util.Collections;

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	TEMP var;
	
	public IRcommand_Load(TEMP dst,TEMP var)
	{
		//dst = string
		this.dst      = dst;
		this.var = var;
		Register_Allocation.getInstance().addCommandToCFG(
				new IR_Node(Collections.singletonList(var.getSerialNumber()),
						dst.getSerialNumber()));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load(dst, var);
	}
}
