package AST;

public class AST_DEC_VARDEC extends AST_DEC{
    public AST_VARDEC vd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_VARDEC(AST_VARDEC vd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT VARDEC RULE */
        /***************************************/
        System.out.print("====================== dec -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /***********************************************/
    /* The default message for an VARDEC AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = VARDEC AST NODE */
        /************************************/
        System.out.print("AST NODE VARDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (vd != null) vd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nDEC\nVARDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);

    }
}
