package AST;

import IR.*;
import TEMP.*;
import TYPES.TYPE;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

    public TYPE SemantMe()
    {
        TYPE t1 = null;
        TYPE t2 = null;

        if (var != null) t1 = var.SemantMe();
        if (exp != null) t2 = exp.SemantMe();

        if (t1 == null || !t1.isLegalAssignment(t2)){
            System.out.format(">> ERROR [%d:%d] illegal assignment \n",2,2);
			this.error();
        }

        return null;
    }

    public TEMP IRme() {
        TEMP t2 = null;
        if (exp != null) t2 = exp.IRme();

        if (var instanceof AST_VAR_SIMPLE){
            IR.getInstance().Add_IRcommand(new IRcommand_Assign(var.IRme(), t2));
        } else if (var instanceof AST_VAR_FIELD) {
            AST_VAR_FIELD varField = (AST_VAR_FIELD) var;
            IR.getInstance().Add_IRcommand(new IRcommand_Field_Set(varField.var.IRme(), varField.fieldName, t2));
        } else if (var instanceof AST_VAR_SUBSCRIPT) {
            AST_VAR_SUBSCRIPT varSubscript = (AST_VAR_SUBSCRIPT) var;
            IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(varSubscript.var.IRme(), varSubscript.subscript.IRme(), t2));
        }
        return null;
    }

}
