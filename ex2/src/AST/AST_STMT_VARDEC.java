package AST;

public class AST_STMT_VARDEC extends AST_STMT {

    public AST_VARDEC vd;

    public AST_STMT_VARDEC(AST_VARDEC vd) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.println("====================== stmt -> varDec");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /*************************************************/
    /* The printing message for a field var AST node */

    /*************************************************/
    public void PrintMe()
    {
        /*********************************/
        /* AST NODE TYPE = AST VARDEC */
        /*********************************/
        System.out.print("AST NODE VARDEC\n");

        /**********************************************/
        /* RECURSIVELY PRINT VARDEC ... */
        /**********************************************/
        if (vd != null) vd.PrintMe();
        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            "AST\nSTMT\nVARDEC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (vd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, vd.SerialNumber);
    }
}
