package AST;

import TEMP.TEMP;
import TYPES.TYPE;

//1
public abstract class AST_ARRAYTYPEDEF extends AST_Node {
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
