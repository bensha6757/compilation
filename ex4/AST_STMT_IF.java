package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body)
	{
		/**********/
		/* SET A UNIQUE SERIAL NUMBER */
		/**********/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/*************/
		System.out.format("====================== exp -> IF (exp) {stmtlist}\n");

		this.cond = cond;
		this.body = body;
	}

	public void PrintMe()
	{
		/*************/
		/* AST NODE TYPE = AST FUNC STMT */
		/*************/
		System.out.print("AST NODE IF STMT\n");

		/**************/
		/* RECURSIVELY PRINT var + func name + exps ... */
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
            System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n",2,2);
			cond.error();
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope("if");

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

    public TEMP IRme()
    {
        /*******************************/
        /* [1] Allocate 2 fresh labels */
        /*******************************/
        String label_end   = IRcommand.getFreshLabel("end");

        /********************/
        /* [3] cond.IRme(); */
        /********************/
        TEMP cond_temp = cond.IRme();

        /******************************************/
        /* [4] Jump conditionally to the loop end */
        /******************************************/
        IR.
            getInstance().
            Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));

        /*******************/
        /* [5] body.IRme() */
        /*******************/
        body.IRme();

        /**********************/
        /* [7] Loop end label */
        /**********************/
        IR.
            getInstance().
            Add_IRcommand(new IRcommand_Label(label_end));

        /*******************/
        /* [8] return null */
        /*******************/
        return null;
    }


}
