package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
    TYPE leftType;
    TYPE rightType;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP = "";

        /*********************************/
        /* CONVERT OP to a printable sOP */
        /*********************************/

        switch (OP) {
            case 0:
                sOP = "+";
                break;
            case 1:
                sOP = "-";
                break;
            case 2:
                sOP = "*";
                break;
            case 3:
                sOP = "/";
                break;
            case 4:
                sOP = "<";
                break;
            case 5:
                sOP = ">";
                break;
            case 6:
                sOP = "=";
                break;
        }

		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST\nBINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

    public TYPE SemantMe()
    {
        if (left  != null) leftType = left.SemantMe();
        if (right != null) rightType = right.SemantMe();
        if (OP == 3 && rightType != null && rightType.isInt()) {
            if (right instanceof AST_EXP_INT && ((AST_EXP_INT) right).value == 0) {
                System.out.format(">> ERROR [%d:%d] illegal division with 0 \n",2,2);
                this.error();

            }
        }
        if (leftType != null && rightType != null){
            if (leftType.isInt() && rightType.isInt()) {
                return TYPE_INT.getInstance();
            } else if (OP == 0 && leftType.isString() && rightType.isString()) {
                return TYPE_STRING.getInstance();
            }
            if (OP == 6){
                if ((!leftType.isInt() && !leftType.isString() && rightType.isNil()) || (!rightType.isInt() && !rightType.isString() && leftType.isNil()) || SYMBOL_TABLE.getInstance().isFather(leftType, rightType) || SYMBOL_TABLE.getInstance().isFather(rightType, leftType) || leftType == rightType){
                    return TYPE_INT.getInstance();
                }
            }
        }
        System.out.format(">> ERROR [%d:%d] illegal Binop operation \n",2,2);
        this.error();
        return null;
    }

    public TEMP IRme()
    {
        TEMP t1 = null;
        TEMP t2 = null;
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

        if (left  != null) t1 = left.IRme();
        if (right != null) t2 = right.IRme();

        switch (OP) {
            case 0:
                if (leftType.isString() && rightType.isString()) {
                    IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Strings(dst,t1,t2));
                }
                else {
                    IR.
                        getInstance().
                        Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));
                }
                break;
            case 1:
                IR.
                    getInstance().
                    Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
                break;
            case 2:
                IR.
                    getInstance().
                    Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
                break;
            case 3:
                IR.
                    getInstance().
                    Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
                break;
            case 4:
                IR.
                    getInstance().
                    Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
                break;
            case 5:
                IR.
                    getInstance().
                    Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
                break;
            case 6:
                if (leftType.isString() && rightType.isString()) {
                    IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst,t1,t2));
                } else {
                    IR.
                        getInstance().
                        Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
                }
                break;
        }
        return dst;
    }
}
