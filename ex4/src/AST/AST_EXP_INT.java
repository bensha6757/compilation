package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_EXP_INT extends AST_EXP
{
	public Integer value;
    private boolean isGlobal = false;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(Integer value)
	{
		System.out.print("---AST_EXP_INT---\n");
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> INT( %d )\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE INT( %d )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("INT(%d)",value));
	}

    public TYPE SemantMe()
    {
        isGlobal = SYMBOL_TABLE.getInstance().checkIfGlobal();
        return TYPE_INT.getInstance();
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (isGlobal) {
            IR.getInstance().Add_Global_Command(new IRcommand_Integer_Expression(dst, value));
        } else {
            IR.getInstance().Add_IRcommand(new IRcommand_Integer_Expression(dst, value));
        }
        return dst;
    }

}
