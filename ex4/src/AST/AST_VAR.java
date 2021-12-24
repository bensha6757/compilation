package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_VAR extends AST_Node {
    String name;
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
