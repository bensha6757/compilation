package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TEMP.*;
import IR.*;

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

    public TEMP IRme() throws FindException {
        TYPE_CLASS cls = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(this.className);

        TEMP obj_size = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP obj_addr = TEMP_FACTORY.getInstance().getFreshTEMP();

        SYMBOL_TABLE.getInstance().beginScope("class");
        this.cFieldList.IRme(cls);
        SYMBOL_TABLE.getInstance().endScope();
        SYMBOL_TABLE.getInstance().curFather = null;


        String vt = "vt_"+this.className;

        // creating label for virtual tabel for the class
        IR.getInstance().Add_IRcommand(new IRcommand_Label(vt));
        ArrayList<Pair<String, String>> functions = cls.get_functions_for_vtable();
        IR.getInstance().Add_IRcommand(new IRcommand_AllocateForVtable(functions));


        IR.getInstance().Add_IRcommand(new IRcommand_Label("func_allocate_object_"+this.id));
        IR.getInstance().Add_IRcommand(new IRcommand_SaveRegisters());

        IR.getInstance().Add_IRcommand(new IRcommandConstInt(obj_size, cls_info.get_object_size()));
        IR.getInstance().Add_IRcommand(new IRcommand_CallFunctionParam(obj_size));
        IR.getInstance().Add_IRcommand(new IRcommand_CallFunction(obj_addr, "Malloc"));

        TEMP vtable_addr = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_GetLabelAddr(vtable_addr, vtable_label));
        IR.getInstance().Add_IRcommand(new IRcommand_StoreToAddressOffset(obj_addr, 0, vtable_addr));

        SYMBOL_TABLE.getInstance().beginClassScope();
        this.cfields.IRme(cls_info, obj_addr);
        SYMBOL_TABLE.getInstance().endScope();

        IR.getInstance().Add_IRcommand(new IRcommand_SetReturnValue(obj_addr));
        IR.getInstance().Add_IRcommand(new IRcommand_RestoreRegisters());
        IR.getInstance().Add_IRcommand(new IRcommand_Return());

        return null;
    }

}
