package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TEMP.*;
import IR.*;

import java.util.List;

public class AST_CLASSDEC_CFIELD extends AST_CLASSDEC {
    String className;
    String parentClassName;
    AST_CFIELD_LIST cFieldList;
    TYPE_CLASS cls;

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
            cls = t;

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

    public TEMP IRme() {
        cls.crateVtable();
        cls.createFieldList();
        cFieldList.IRme();

        String vtLabel = "vt_112233_" + className;
        IR.getInstance().Add_IRcommand(new IRcommand_Label(vtLabel));

        List<List<String>> vtable = cls.getVtable();
        IR.getInstance().Add_IRcommand(new IRcommand_Allocate_Vtable(vtable, className));


        // function for auto constructor
        TEMP thisInstance = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Label("constructor_" + className));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Prologue(0));

        IR.getInstance().Add_IRcommand(new IRcommand_Load_This_Instance(thisInstance));
        cFieldList.IRme(thisInstance);

        IR.getInstance().Add_IRcommand(new IRcommand_Return(thisInstance));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue());

        // function for allocating new instance
        TEMP newInstance = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Label("allocate_new_instance_for_class_"+this.className));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Prologue(0));

        IR.getInstance().Add_IRcommand(new IRcommand_Allocate_New_Instance(newInstance, cls.getClassSize(), vtLabel));
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Function_EXP(newInstance, "constructor_" + className, new TEMP_LIST(newInstance, null)));

        TYPE_CLASS fatherCls = cls.father;
        while (fatherCls != null) {
            IR.getInstance().Add_IRcommand(new IRcommand_Call_Function_EXP(newInstance, "constructor_" + fatherCls.name, new TEMP_LIST(newInstance, null)));
            fatherCls = fatherCls.father;
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Return(newInstance));
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue());

        return null;
    }

}
