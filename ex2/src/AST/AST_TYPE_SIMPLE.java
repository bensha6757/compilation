package AST;

public class AST_TYPE_SIMPLE extends AST_TYPE{
    /************************/
    /* simple variable name */
    /************************/
    public String typeName;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_TYPE_SIMPLE(String typeName)
    {
        System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + typeName + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== type -> ID( %s )\n",typeName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.typeName = typeName;
    }

    /**************************************************/
    /* The printing message for a simple type AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE TYPE */
        /**********************************/
        System.out.format("AST NODE SIMPLE TYPE( %s )\n",typeName);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_ID(%s)\n",typeName));
    }
}
