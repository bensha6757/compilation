package AST;

public class AST_NEWEXP_NEW extends AST_NEWEXP{
    AST_TYPE t;
    AST_EXP exp;
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_NEWEXP_NEW(AST_TYPE t, AST_EXP exp)
    {
        System.out.println(exp);
        System.out.println(t);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== newExp -> NEW ID( %s )\n", t);
        System.out.format("====================== newExp -> NEW ID( %s ) [exp]\n", t);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.t = t;
        this.exp = exp;
    }

    /*************************************************/
    /* The printing message for a newExp new AST node */
    /*************************************************/
    public void PrintMe()
    {
        /*********************************/
        /* AST NODE TYPE = AST NEWEXP NEW */
        /*********************************/
        System.out.print("AST NODE NEWEXP NEW\n");

        /**********************************************/
        /* , PRINT CLASS NAME then RECURSIVELY PRINT EXP ... */
        /**********************************************/
        System.out.format("TYPE( %s )\n",t);
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\nNEWEXP\nNEW\n...->##%s##",t));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }

}
