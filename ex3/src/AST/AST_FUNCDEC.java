package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public abstract class AST_FUNCDEC extends AST_Node {
    AST_TYPE type;
    String funcName;
    AST_STMT_LIST stmts;
    AST_TYPEID_LIST typeIds;

    public abstract TYPE SemantMe();
    public abstract void SemantMe(TYPE_CLASS cls);
}
