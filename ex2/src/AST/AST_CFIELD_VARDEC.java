package AST;

public class AST_CFIELD_VARDEC extends AST_CFIELD{
    public AST_VARDEC vd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_CFIELD_VARDEC(AST_VARDEC vd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT VARDEC RULE */
        /***************************************/
        System.out.print("====================== cField -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /***********************************************/
    /* The default message for an CFIELD VARDEC AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = CFIELD VARDEC AST NODE */
        /************************************/
        System.out.print("AST NODE  CFIELD VARDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (vd != null) vd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nCFIELD\nVARDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);

    }
}
