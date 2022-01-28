package AST;

import SYMBOL_TABLE.FindException;
import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_CLASSDEC extends AST_Node {
    public abstract TYPE SemantMe();
    public abstract TEMP IRme() throws FindException;
}
