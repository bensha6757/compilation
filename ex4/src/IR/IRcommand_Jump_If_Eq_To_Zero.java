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

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand
{
	TEMP t;
	String label_name;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name)
	{
		this.t          = t;
		this.label_name = label_name;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(
            Collections.singletonList(t.getSerialNumber()), -1, false, true, false, label_name));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().beqz(t,label_name);
	}
}
