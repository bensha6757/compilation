package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;

public class AST_TYPE_SIMPLE extends AST_TYPE {
    /************************/
    /* simple variable name */
    /************************/


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_TYPE_SIMPLE(String typeName)
    {
        System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + typeName + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== type -> ID( %s )\n",typeName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.typeName = typeName;
    }


    /**************************************************/
    /* The printing message for a simple type AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE TYPE */
        /**********************************/
        System.out.format("AST NODE SIMPLE TYPE( %s )\n",typeName);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_ID(%s)\n",typeName));
    }

    @Override
    public TYPE SemantMe() {
        TYPE t = null;
        try{
            t = SYMBOL_TABLE.getInstance().find(typeName);
        }
        catch (FindException e){
            this.error();
        }
        if (t == null)
        {
            /**************************/
            /* ERROR: undeclared type */
            /**************************/
            System.out.format(">> ERROR [%d:%d] type does not exist\n",4,4);
            this.error();
        }

        if (!t.isClass() && !t.isArray()){
            System.out.format(">> ERROR [%d:%d] %s doesn't define a type\n",4,4, typeName);
            this.error();
        }

        /****************************/
        /* return (existing) type t */
        /****************************/
        return t;
    }

    @Override
    public TEMP IRme() {
        return null;
    }
}
