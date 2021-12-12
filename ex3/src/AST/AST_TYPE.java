package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public abstract class AST_TYPE extends AST_Node {
    public String typeName;
    public abstract TYPE SemantMe();
}
