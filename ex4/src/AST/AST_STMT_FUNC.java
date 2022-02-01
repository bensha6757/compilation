package AST;

import IR.*;
import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;

public class AST_STMT_FUNC extends AST_STMT {
    public String name;
    public AST_VAR var;
    public AST_EXP_LIST exps;

    private TYPE_CLASS cls;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_STMT_FUNC(AST_VAR var, String name, AST_EXP_LIST exps)
    {
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== exp -> var.ID( %s )(exps)\n", name);

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.var = var;
        this.exps = exps;
        this.name = name;
    }

    /*****************/
    /* The printing message for a func exp AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST FUNC STMT */
        /*************/
        System.out.print("AST NODE FUNC STMT\n");

        /**************/
        /* RECURSIVELY PRINT var + func name + exps ... */
        /**************/
        if (var != null) var.PrintMe();
        System.out.format("FUNCTION NAME( %s )\n",name);
        if (exps != null) exps.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\nSTMT\nFUNC(%s)",name));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        TYPE t = null;
        TYPE varType = null;
        TYPE_LIST params = null;

        /*******/
        /* [0] return type */
        /*******/
        if (var != null){
            try{
                varType = SYMBOL_TABLE.getInstance().find(var.name);
            }
            catch (FindException e){
                this.error();
            }
            if (varType == null) {
                System.out.format(">> ERROR [%d:%d] non existing var type %s\n", 6, 6, varType);
                this.error();
            }
            cls = (TYPE_CLASS) varType;
        }
        if (exps != null){
            params = exps.SemantMe();
        }


        /**************/
        /* [2] Check That Name does NOT exist */
        /**************/
        try {
            if (varType != null){
                t = SYMBOL_TABLE.getInstance().find(name,  params, true, (TYPE_CLASS)varType);
            }
            else {
                t = SYMBOL_TABLE.getInstance().find(name, null, params, true);
            }
            if (t == null){
                System.out.format(">> ERROR [%d:%d] illegal call function %s\n",2,2,name);
                this.error();
            }
        } catch (FindException e) {
            System.out.format(">> ERROR [%d:%d] illegal override function %s\n",2,2,name);
            this.error();
        }

        if (var == null)
            cls = SYMBOL_TABLE.getInstance().getLowestClass();


        /*******************/
        /* [6] Return value is irrelevant for class declarations */
        /*******************/
        return ((TYPE_FUNCTION)t).returnType;
    }

    public TEMP IRme()
    {
        TEMP t0 = null, t;
        TEMP_LIST paramsTemps = null;
        if (var != null) t0 = var.IRme();
        if (exps != null){
            paramsTemps = new TEMP_LIST();
            if (exps.head != null){
                t = exps.head.IRme();
                paramsTemps.addAtFirst(t);
            }
            AST_EXP_LIST it = exps.tail;
            while (it != null)
            {
                it = it.tail;
                t = it.head.IRme();
                paramsTemps.addAtFirst(t);
            }
        }

        if (name.equals("PrintInt")) {
            IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(paramsTemps.head));
        } else if (name.equals("PrintString")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Print_String(paramsTemps.head));
        } else {
            TEMP thisInstance = TEMP_FACTORY.getInstance().getFreshTEMP();
            if (var == null){
                if (cls != null && cls.getVtableOffset(name) != -1) { // a class-function was called
                    IR.getInstance().Add_IRcommand(new IRcommand_Load_This_Instance(thisInstance));
                    IR.getInstance().Add_IRcommand(new IRcommand_Virtual_Call_Function_STMT(thisInstance, cls.getVtableOffset(name), paramsTemps));
                } else { // global
                    IR.getInstance().Add_IRcommand(new IRcommand_Call_Function_STMT("func_g_" + name, paramsTemps));
                }
            } else {
                IR.getInstance().Add_IRcommand(new IRcommand_Virtual_Call_Function_STMT(t0, cls.getVtableOffset(name), paramsTemps));
            }
        }
        return null;
    }

}
