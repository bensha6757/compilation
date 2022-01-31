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
	String var_name;
	
	public IRcommand_Load(TEMP dst,String var_name)
	{
		//dst = string
		this.dst      = dst;
		this.var_name = var_name;
		Register_Allocation.getInstance().addCommandToCFG(
				new IR_Node(Collections.emptyList(),
						dst.getSerialNumber()));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load(dst,var_name);
	}
}
