package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_TYPE extends AST_Node {
    public String typeName;
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
