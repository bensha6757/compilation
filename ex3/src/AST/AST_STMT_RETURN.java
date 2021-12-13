package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
public class AST_STMT_RETURN extends AST_STMT {
    /***************/
    /*  return exp; */
    /***************/
    public AST_EXP exp;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_STMT_RETURN(AST_EXP exp)
    {
        System.out.print("---AST_STMT_RETURN---");

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> RETURN exp SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST RETURN STATEMENT */
        /********************************************/
        System.out.print("AST NODE RETURN STMT\n");

        /***********************************/
        /* RECURSIVELY PRINT EXP ... */
        /***********************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            "AST\nSTMT\nRETURN\nexp;\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        TYPE_FUNCTION myFunc = SYMBOL_TABLE.getInstance().getMyFunc();
        TYPE expressionType = exp.SemantMe();
        if ((myFunc.returnType.isVoid() && expressionType != null) ||
            (!myFunc.returnType.isVoid() && expressionType == null) ||
            ((myFunc.returnType.isString() || myFunc.returnType.isInt()) && expressionType != null && expressionType.isNil()) ||
            (expressionType != null && !expressionType.isNil() && myFunc.returnType != expressionType) ){
            System.out.format("return statement type mismatch return %s while return type should be %s", myFunc.returnType.name, expressionType.name);
            this.error();
        }
        return expressionType;
    }
}
