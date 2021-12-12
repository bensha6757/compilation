package AST;

public class AST_VARDEC_TYPEEXP extends AST_VARDEC{
    /************************/
    /* simple variable name */
    /************************/
    public AST_TYPE t;
    public String varName;
    public AST_EXP e;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_VARDEC_TYPEEXP(AST_TYPE t, String varName, AST_EXP e)
    {
        System.out.println("---AST_VARDEC_TYPEEXP---");
        System.out.println(e);

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== varDec ->  type ID( %s ) [ ASSIGN exp ];\n",varName);
        System.out.format("====================== varDec ->  type ID( %s ) ASSIGN newExp;\n",varName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.varName = varName;
        this.t = t;
        this.e = e;
    }

    /*****************************************************/
    /* The printing message for a varDec TYPEEXP AST node */
    /*****************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST varDec TYPEEXP */
        /*************************************/
        System.out.print("AST NODE varDec TYPEEXP\n");

        /****************************************/
        /* PRINT NAME+ TYPE + EXP ... */
        /****************************************/
        if (t != null) t.PrintMe();
        System.out.format("AST NODE SIMPLE VAR( %s )\n",varName);
        if (e != null) e.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST\nVARDEC\nTYPEEXP\n(%s)",varName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (t != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
        if (e != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
    }
}
