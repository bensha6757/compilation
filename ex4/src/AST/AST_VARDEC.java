package AST;

import TEMP.TEMP;
import TYPES.TYPE_CLASS;

public abstract class AST_VARDEC extends AST_Node {
    public AST_TYPE type;
    public String varName;
    public AST_EXP exp;

    public abstract void SemantMe(TYPE_CLASS cls);
    public abstract TEMP IRme();
    public abstract TEMP IRme(TEMP thisInstance);
}
