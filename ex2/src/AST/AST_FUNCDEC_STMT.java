package AST;

public class AST_FUNCDEC_STMT extends AST_FUNCDEC {

    AST_TYPE type;
    String funcName;
    AST_TYPEID_LIST typeIds;
    AST_STMT_LIST stmts;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_FUNCDEC_STMT(AST_TYPE type, String funcName, AST_TYPEID_LIST typeIds, AST_STMT_LIST stmts)
    {
        System.out.print("---AST_FUNCDEC_STMT---");
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== funcDec -> type ID( %s )(typeIds) {stmts}\n", funcName);

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.type = type;
        this.funcName = funcName;
        this.typeIds = typeIds;
        this.stmts = stmts;
    }

    /*****************/
    /* The printing message for class dec func AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST FUNCDEC STMT */
        /*************/
        System.out.print("AST NODE FUNCDEC STMT\n");

        /**************/
        /* RECURSIVELY PRINT type + funcName + typeIds + stmts ... */
        /**************/
        if (type != null) type.PrintMe();
        System.out.format("FUNC NAME( %s )\n", funcName);
        if (typeIds != null) typeIds.PrintMe();
        if (stmts != null) stmts.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\nFUNCDEC\nSTMT\n(%s)",funcName));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (typeIds != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeIds.SerialNumber);
        if (stmts != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmts.SerialNumber);
    }

}
