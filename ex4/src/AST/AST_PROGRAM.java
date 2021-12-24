package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_PROGRAM extends AST_Node {
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
