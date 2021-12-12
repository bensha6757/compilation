package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public abstract class AST_VARDEC extends AST_Node {
    public AST_TYPE type;
    public String varName;
    public AST_EXP exp;

    public abstract void SemantMe(TYPE_CLASS cls);
}
