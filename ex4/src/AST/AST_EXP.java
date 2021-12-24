package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_EXP extends AST_Node
{
	public int moish;
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();

}
