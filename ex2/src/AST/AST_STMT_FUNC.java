package AST;

public class AST_STMT_FUNC extends AST_STMT{
    public String name;
    public AST_VAR var;
    public AST_EXP_LIST exps;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_STMT_FUNC(AST_VAR var,String name,AST_EXP_LIST exps)
    {
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== exp -> var.ID( %s )(exps)\n", name);

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.var = var;
        this.exps = exps;
        this.name = name;
    }

    /*****************/
    /* The printing message for a func exp AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST FUNC STMT */
        /*************/
        System.out.print("AST NODE FUNC STMT\n");

        /**************/
        /* RECURSIVELY PRINT var + func name + exps ... */
        /**************/
        if (var != null) var.PrintMe();
        System.out.format("FUNCTION NAME( %s )\n",name);
        if (exps != null) exps.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\nSTMT\nFUNC(%s)",name));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
    }
}
