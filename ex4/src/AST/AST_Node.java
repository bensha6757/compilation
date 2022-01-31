package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
    public int LineNumber;

	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public void error()
    {
		throw new IllegalStateException("ERROR("+LineNumber+")\n");
    }
	
	public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
