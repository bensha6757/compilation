package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public abstract class AST_VAR extends AST_Node {
    String name;
    public abstract TYPE SemantMe();
}
