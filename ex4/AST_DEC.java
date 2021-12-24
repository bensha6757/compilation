package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_DEC extends AST_Node
{
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
