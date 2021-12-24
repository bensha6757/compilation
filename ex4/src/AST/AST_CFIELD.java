package AST;

import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public abstract class AST_CFIELD extends AST_Node {
    String name;

    public abstract TYPE SemantMe();
    public abstract void SemantMe(TYPE_CLASS cls);
    public abstract TEMP IRme();
}
