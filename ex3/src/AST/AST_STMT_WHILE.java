package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond, AST_STMT_LIST body)
	{
		/**********/
		/* SET A UNIQUE SERIAL NUMBER */
		/**********/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/*************/
		System.out.format("====================== exp -> WHILE (exp) {stmtlist}\n");

		this.cond = cond;
		this.body = body;
	}

	public void PrintMe()
	{
		/*************/
		/* AST NODE TYPE = AST FUNC STMT */
		/*************/
		System.out.print("AST NODE WHILE STMT\n");

		/**************/
		/* RECURSIVELY PRINT var + while name + exps ... */
		/**************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();


		/**************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/**************/
		if (cond  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

    public TYPE SemantMe()
    {
        /****************************/
        /* [0] Semant the Condition */
        /****************************/
        if (cond.SemantMe() != TYPE_INT.getInstance())
        {
            System.out.format(">> ERROR [%d:%d] condition inside WHILE is not integral\n",2,2);
			this.error();
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope("while");

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        body.SemantMe();

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /*********************************************************/
        /* [4] Return value is irrelevant for if statement */
        /*********************************************************/
        return null;
    }
}
