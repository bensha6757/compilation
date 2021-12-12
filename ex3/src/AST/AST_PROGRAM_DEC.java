package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public class AST_PROGRAM_DEC extends AST_PROGRAM {
    public AST_DEC_LIST l;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PROGRAM_DEC(AST_DEC_LIST l)
    {
        System.out.print("---PROGRAM---\n");
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== Program -> dec decs\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.l = l;
    }

    /***********************************************/
    /* The default message for an program dec AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = PROGRAM DEC AST NODE */
        /************************************/
        System.out.print("AST NODE PROGRAM DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT l ... */
        /*****************************/
        if (l != null) l.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nPROGRAM\nDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);

    }

    @Override
    public TYPE SemantMe() {
        return l.SemantMe();
    }
}

