package AST;

public class AST_TYPEID_LIST extends AST_Node{
    /****************/
    /* DATA MEMBERS */
    /****************/

    public AST_TYPE head;
    public String name;
    public AST_TYPEID_LIST tail;


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_TYPEID_LIST(AST_TYPE head, String name, AST_TYPEID_LIST tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== typeids -> typeid , typeids\n");
        if (tail == null) System.out.print("====================== typeids -> typeid      \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
        this.name = name;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/
        System.out.print("AST NODE TYPEID LIST\n");
        System.out.format("AST NODE ID IN TYPEID LIST( %s )\n",name);

        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (head != null) head.PrintMe();
        if (tail != null) tail.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST\nTYPEID\nLIST\n");
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPEID\nLIST\n(%s)",name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }
}
