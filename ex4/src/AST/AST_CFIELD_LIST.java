package AST;

import TYPES.TYPE_CLASS;
import TYPES.TYPE_LIST;
import TEMP.*;

public class AST_CFIELD_LIST extends AST_Node {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_CFIELD head;
    public AST_CFIELD_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== cFields -> cField cFields\n");
        if (tail == null) System.out.print("====================== cFields -> cField      \n");

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
        System.out.print("AST NODE CFIELD LIST\n");

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
                "AST\nCFIELD\nLIST\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }

    public void SemantMe(TYPE_CLASS cls) {
        head.SemantMe(cls);
        if (tail != null) {
            tail.SemantMe(cls);
        }
    }

    public TYPE_LIST SemantMe()
    {
        SemantMe(null);
        return null;
    }

    // at the data section there are the functions of the class
    public TEMP IRme(TYPE_CLASS cls) {
        TYPE_LIST tail_list = null;
        if ((this.head instanceof AST_CFIELD_FUNCDEC)) {
            this.head.IRme(cls);
        }
        if (this.tail != null) {
            this.tail.IRme(cls);
        }
        return null;
    }



}
