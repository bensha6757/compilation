package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_LIST extends AST_Node {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_EXP head;
    public AST_EXP_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_LIST(AST_EXP head, AST_EXP_LIST tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== exps -> exp , exps\n");
        if (tail == null) System.out.print("====================== exps -> exp      \n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/
        System.out.print("AST NODE EXP LIST\n");

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
            "AST\nEXP\nLIST\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }
    public TYPE_LIST SemantMe()
    {
        if (head == null) {
            return new TYPE_LIST(null, "", null);
        }
        return SemantTheList();
    }

    public TYPE_LIST SemantTheList() {
        if (tail == null)
        {
            return new TYPE_LIST(
                head.SemantMe(),
                "",
                null);
        }
        else
        {
            return new TYPE_LIST(
                    head.SemantMe(),
                    "",
                    tail.SemantMe());
        }
    }
}
