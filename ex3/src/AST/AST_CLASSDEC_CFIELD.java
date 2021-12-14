package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.*;
import TYPES.*;
public class AST_CLASSDEC_CFIELD extends AST_CLASSDEC {
    String className;
    String parentClassName;
    AST_CFIELD_LIST cFieldList;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_CLASSDEC_CFIELD(String className, String parentClassName, AST_CFIELD_LIST cFieldList)
    {
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== classDec -> CLASS ID( %s )[extends ID( %s )] {cFields}\n", className, parentClassName != null ? parentClassName : "");

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.className = className;
        this.parentClassName = parentClassName;
        this.cFieldList = cFieldList;
    }

    /*****************/
    /* The printing message for class dec cField AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST CLASSDEC CFIELD */
        /*************/
        System.out.print("AST NODE CLASSDEC CFIELD\n");

        /**************/
        /* RECURSIVELY PRINT className + parentClassName + cFieldList ... */
        /**************/
        System.out.format("CLASS NAME( %s )\n", className);
        if (parentClassName != null) System.out.format("PARENT CLASS NAME( %s )\n", parentClassName);
        if (cFieldList != null) cFieldList.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("CLASS\n(%s)",className));
        if (parentClassName != null) AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("PARENT\nCLASS\n(%s)",parentClassName));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (cFieldList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cFieldList.SerialNumber);
    }

    public TYPE SemantMe()
    {
        if (!SYMBOL_TABLE.getInstance().checkIfGlobal()){
            System.out.format(">> ERROR [%d:%d] class wasn't defined globally\n",9,9);
            this.error();
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().checkIfVarExistsInCurrentScope(className))
        {
            System.out.format(">> ERROR [%d:%d] function %s already exists in scope\n",2,2,className);
            this.error();
        }


        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS t = null, father = null;
        try{
            if (parentClassName != null){
                father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(parentClassName);
                if (father == null){
                    System.out.format(">> ERROR [%d:%d] parent class not found %s\n",9,9, parentClassName);
                    this.error();
                }
            }
            SYMBOL_TABLE.getInstance().curFather = father;
            t = new TYPE_CLASS(father, className,null);
            SYMBOL_TABLE.getInstance().enter(className,t);

            /*************************/
            /* [1] Begin Class Scope */
            /*************************/
            SYMBOL_TABLE.getInstance().beginScope("class");
            if (cFieldList != null){
                cFieldList.SemantMe(t);
            }
        }
        catch (FindException e){
            this.error();
        }

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();
        SYMBOL_TABLE.getInstance().curFather = null;
        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/


        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }

}
