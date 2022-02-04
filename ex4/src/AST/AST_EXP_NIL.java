package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {
    private boolean isGlobal = false;

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
        isGlobal = SYMBOL_TABLE.getInstance().checkIfGlobal();
        return TYPE_NIL.getInstance();
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (isGlobal) {
            IR.getInstance().Add_Global_Command(new IRcommand_NIL(dst));
        } else {
            IR.getInstance().Add_IRcommand(new IRcommand_NIL(dst));
        }
        return dst;
    }
}
