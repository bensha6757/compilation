package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import IR.*;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
    TYPE_CLASS cls = null;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST\nVAR\nSIMPLE\n(%s)",name));
	}

	public TYPE SemantMe()
	{
		TYPE t = null;
		try{
			t = SYMBOL_TABLE.getInstance().find(name);
		}
		catch (FindException e){
			this.error();
		}
        if (t == null){
            System.out.format(">> ERROR [%d:%d] ID %s does not exist\n",6,6,this.name);
			this.error();
        } else {
            if (t.isClass())
                cls = (TYPE_CLASS) t;
        }
		return t;
	}

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (IR.getInstance().isLocalVarExists(name)) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Variable(dst, name));
        } else if (cls != null && cls.getFieldOffset(name) != -1) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_This_Instance(dst, cls.getFieldOffset(name)));
        } else {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Global_Variable(dst, name));
        }
        return dst;
    }

}
