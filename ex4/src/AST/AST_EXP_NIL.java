package AST;

import IR.*;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {

    public AST_EXP_NIL(){
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    public void PrintMe()
    {
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NIL");
    }
    
    public TYPE SemantMe()
    {
        return TYPE_NIL.getInstance();
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_NIL(dst));
        return dst;
    }
}
