package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;

public class AST_ARRAYTYPEDEF_ID extends AST_ARRAYTYPEDEF {
    /************************/
    /* simple variable name */
    /************************/
    public String arrayName;
    public AST_TYPE t;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_ARRAYTYPEDEF_ID(String arrayName, AST_TYPE t)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== arrayTypedef -> ARRAY ID( %s ) = type [];\n",arrayName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.arrayName = arrayName;
        this.t = t;
    }

    /**************************************************/
    /* The printing message for a arraytypedef id AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE VAR */
        /**********************************/
        System.out.format("AST NODE  ID( %s ) ARRAYTYPEDEF\n",arrayName);
        if (t != null) t.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST\nARRAYTYPEDEF\nID(%s)",arrayName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (t  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,t.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        if (!SYMBOL_TABLE.getInstance().checkIfGlobal()){
            System.out.format(">> ERROR [%d:%d] array wasn't defined globally\n",9,9);
            this.error();
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().checkIfVarExistsInCurrentScope(arrayName))
        {
            System.out.format(">> ERROR [%d:%d] function %s already exists in scope\n",2,2,arrayName);
            this.error();
        }

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE type = t.SemantMe();
        System.out.println(type.name);
        if (type == null){
            System.out.format(">> ERROR [%d:%d] void type is illegal %s\n",2,2,t.typeName);
            this.error();
        }
        TYPE_ARRAY typeArray = new TYPE_ARRAY(arrayName, type);

        /************************************************/
        /* [4] Enter the Array Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(arrayName,typeArray);

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;

    }

    @Override
    public TEMP IRme() {
        return null;
    }
}
