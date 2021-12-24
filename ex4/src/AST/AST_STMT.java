package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
