package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_NEWEXP_NEW extends AST_NEWEXP {
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
        System.out.format("====================== newExp -> NEW type( %s )\n", t);
        System.out.format("====================== newExp -> NEW type( %s ) [exp]\n", t);

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


    @Override
    public TYPE SemantMe() {
        TYPE expType;
        TYPE type = t.SemantMe();
        if (exp != null){
            expType = exp.SemantMe();
            if (expType.isInt() && (exp instanceof AST_EXP_INT) && (((AST_EXP_INT)exp).value <= 0)) {
                System.out.format(">> ERROR [%d:%d] expression is non positive constant %s\n",2,2,expType.name);
                this.error();
                //System.exit(0);
            } else if (!expType.isInt()) {
                System.out.format(">> ERROR [%d:%d] expression is not an integer type %s\n",2,2,expType.name);
                this.error();
                //System.exit(0);
            }
            return new TYPE_ARRAY_RIGHT(type);
        }
        return type;
    }

}
