package AST;

public class AST_DEC_CLASSDEC extends AST_DEC{
    public AST_CLASSDEC cd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_CLASSDEC(AST_CLASSDEC cd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CLASSDEC RULE */
        /***************************************/
        System.out.print("====================== dec -> classDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.cd = cd;
    }

    /***********************************************/
    /* The default message for an CLASSDEC AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = CLASSDEC AST NODE */
        /************************************/
        System.out.print("AST NODE CLASSDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (cd != null) cd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nDEC\nCLASSDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cd.SerialNumber);

    }
}
