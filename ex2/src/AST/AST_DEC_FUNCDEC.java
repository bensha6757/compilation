package AST;

public class AST_DEC_FUNCDEC extends AST_DEC{
    public AST_FUNCDEC fd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_FUNCDEC(AST_FUNCDEC fd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT FUNCDEC RULE */
        /***************************************/
        System.out.print("====================== dec -> funcDec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.fd = fd;
    }

    /***********************************************/
    /* The default message for an FUNCDEC AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = FUNCDEC AST NODE */
        /************************************/
        System.out.print("AST NODE FUNCDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (fd != null) fd.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nDEC\nFUNCDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fd.SerialNumber);

    }
}
