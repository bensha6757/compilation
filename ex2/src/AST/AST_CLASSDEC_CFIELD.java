package AST;

public class AST_CLASSDEC_CFIELD extends AST_CLASSDEC{
    String className;
    String parentClassName;
    AST_CFIELD_LIST cFieldList;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_CLASSDEC_CFIELD(String className, String parentClassName, AST_CFIELD_LIST cFieldList)
    {
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== classDec -> CLASS ID( %s )[extends ID( %s )] {cFields}\n", className, parentClassName != null ? parentClassName : "");

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.className = className;
        this.parentClassName = parentClassName;
        this.cFieldList = cFieldList;
    }

    /*****************/
    /* The printing message for class dec cField AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST CLASSDEC CFIELD */
        /*************/
        System.out.print("AST NODE CLASSDEC CFIELD\n");

        /**************/
        /* RECURSIVELY PRINT className + parentClassName + cFieldList ... */
        /**************/
        System.out.format("CLASS NAME( %s )\n", className);
        if (parentClassName != null) System.out.format("PARENT CLASS NAME( %s )\n", parentClassName);
        if (cFieldList != null) cFieldList.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("CLASS\n(%s)",className));
        if (parentClassName != null) AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("PARENT\nCLASS\n(%s)",parentClassName));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cFieldList.SerialNumber);
    }

}
