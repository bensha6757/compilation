package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var, String fieldName)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST\nVAR\nFIELD\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE t = null, t2 = null;
		TYPE_CLASS tc = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
        if (var == null){
            System.out.format(">> ERROR [%d:%d] var is null\n",6,6);
			this.error();
            //System.exit(0);
        }
        t = var.SemantMe();
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] var type does not exist for var %s\n",6,6,this.var.name);
			this.error();
            //System.exit(0);
        }

		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (!t.isClass())
		{
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,this.fieldName);
			this.error();
			//System.exit(0);
		}
		else
		{
			tc = (TYPE_CLASS) t;
		}

		/************************************/
		/* [3] Look for fieldName inside tc */
		/************************************/
		try{
			t2 = SYMBOL_TABLE.getInstance().find(fieldName, null, false, tc);
		}
		catch (FindException e){
			this.error();
		}
        if (t2 == null){
            /*********************************************/
            /* [4] fieldName does not exist in class var */
            /*********************************************/
            System.out.format(">> ERROR [%d:%d] field %s is not a known field of var %s\n",6,6,this.fieldName, this.var.name);
			this.error();
            //System.exit(0);
        }
		return t2;
	}
}
