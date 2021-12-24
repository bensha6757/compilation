package AST;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
        this.name = var.name;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"AST\nVAR\nSUBSCRIPT\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE varType, subscriptType;

		/****************************/
		/* [1] Check If var exists and is array */
		/****************************/
        varType = var.SemantMe();
		if ((varType == null) || !varType.isArray())
		{
			System.out.format(">> ERROR [%d:%d] var is not an array %s\n",2, 2, var.name);
			this.error();
		}

        subscriptType = subscript.SemantMe();
		if(subscriptType.isInt() && (subscript instanceof AST_EXP_INT) && (((AST_EXP_INT)subscript).value < 0)) {
			System.out.format(">> ERROR [%d:%d] subscript expression is negative constant %s\n",2,2,subscriptType.name);
			this.error();
		} else if (!subscriptType.isInt()) {
			System.out.format(">> ERROR [%d:%d] subscript expression is not an integer type %s\n",2,2,subscriptType.name);
			this.error();
		}

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return ((TYPE_ARRAY)varType).type;
	}
}
