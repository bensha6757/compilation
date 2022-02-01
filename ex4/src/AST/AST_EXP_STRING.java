package AST;

import IR.*;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_STRING;

public class AST_EXP_STRING extends AST_EXP {
    public String str;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_EXP_STRING(String str)
    {
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== exp -> STRING( %s )\n", str);

        /***********/
        /* COPY INPUT DATA NENBERS ... */
        /***********/
        String newStr = str.substring(1, str.length()-1);
        this.str = newStr;
    }

    /****************/
    /* The printing message for an STRING EXP AST node */
    /****************/
    public void PrintMe()
    {
        /***********/
        /* AST NODE TYPE = AST STRING EXP */
        /***********/
        System.out.format("AST NODE STRING( %s )\n",str);

        /***********/
        /* Print to AST GRAPHIZ DOT file */
        /***********/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("STRING\n(%s)",str));
    }

    public TYPE SemantMe()
    {
        return TYPE_STRING.getInstance();
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_String_Expression(str, dst));
        IR.getInstance().addString(str);
        return dst;
    }
}
