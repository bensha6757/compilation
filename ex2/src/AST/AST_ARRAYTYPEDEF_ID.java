package AST;

public class AST_ARRAYTYPEDEF_ID extends AST_ARRAYTYPEDEF{
    /************************/
    /* simple variable name */
    /************************/
    public String arrayName;
    public AST_TYPE t;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_ARRAYTYPEDEF_ID(String arrayName, AST_TYPE t)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== arrayTypedef -> ARRAY ID( %s ) = type [];\n",arrayName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.arrayName = arrayName;
        this.t = t;
    }

    /**************************************************/
    /* The printing message for a arraytypedef id AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE VAR */
        /**********************************/
        System.out.format("AST NODE  ID( %s ) ARRAYTYPEDEF\n",arrayName);
        if (t != null) t.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST\nARRAYTYPEDEF\nID(%s)",arrayName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (t  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
    }
}
