package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
public abstract class AST_CFIELD extends AST_Node {
    String name;

    public abstract TYPE SemantMe();
    public abstract void SemantMe(TYPE_CLASS cls);
}
