package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public class AST_CFIELD_FUNCDEC extends AST_CFIELD {
    public AST_FUNCDEC fd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_CFIELD_FUNCDEC(AST_FUNCDEC fd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT FUNCDEC RULE */
        /***************************************/
        System.out.print("====================== cField -> funcDec\n\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.fd = fd;
        this.name = fd.funcName;
    }

    /***********************************************/
    /* The default message for an CFIELD FUNCDEC AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = CFIELD FUNCDEC AST NODE */
        /************************************/
        System.out.print("AST NODE CFIELD FUNCDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (fd != null) fd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nCFIELD\nFUNCDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fd.SerialNumber);

    }

    public void SemantMe(TYPE_CLASS cls)
    {
        fd.SemantMe(cls);
    }


    public TYPE SemantMe()
    {
        this.SemantMe(null);
        return null;
    }
}
