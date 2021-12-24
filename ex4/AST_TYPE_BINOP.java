package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

public class AST_TYPE_BINOP extends AST_TYPE {
    int OP;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_TYPE_BINOP(int OP)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if(OP == 0){
            System.out.print("---TYPE_INT---\n");
            typeName = "int";
        }
        if(OP == 1){
            System.out.print("====================== type -> TYPE_STRING\n");
            typeName = "string";
        }
        if(OP == 2){
            System.out.print("====================== type -> TYPE_VOID\n");
            typeName = "void";
        }

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.OP = OP;
    }

    /*************************************************/
    /* The printing message for a binop exp AST node */
    /*************************************************/
    public void PrintMe()
    {
        String sOP = "";
        switch (OP) {
            case 0:{
                sOP = "INT";
                break;
            }
            case 1:{
                sOP = "STRING";
                break;
            }
            case 2:{
                sOP = "VOID";
                break;
            }
            /*********************************/
            /* CONVERT OP to a printable sOP */
            /*********************************/
        };

        /*************************************/
        /* AST NODE TYPE = AST TYPE */
        /*************************************/
        System.out.print("AST NODE TYPE\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_%s",sOP));
    }

    @Override
    public TYPE SemantMe() {
        TYPE t = null;
        try{
            t = SYMBOL_TABLE.getInstance().find(typeName);
        }
        catch (FindException e){
            System.out.format(">> ERROR [%d:%d] type does not exist\n",4,4);
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
        /****************************/
        /* return (existing) type t */
        /****************************/
        return t;
    }

}
