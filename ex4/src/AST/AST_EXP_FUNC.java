package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;
import TEMP.*;
import IR.*;

public class AST_EXP_FUNC extends AST_EXP {
    public String name;
    public AST_VAR var;
    public AST_EXP_LIST exps;

    private TYPE_CLASS cls;
    /**/
    /* CONSTRUCTOR(S) */
    /**/
    public AST_EXP_FUNC(AST_VAR var, String name, AST_EXP_LIST exps)
    {
        /**/
        /* SET A UNIQUE SERIAL NUMBER */
        /**/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***/
        System.out.format("====================== exp -> var.ID( %s )(exps)\n", name);

        /***/
        /* COPY INPUT DATA MEMBERS ... */
        /***/
        this.var = var;
        this.exps = exps;
        this.name = name;
    }

    /***/
    /* The printing message for a func exp AST node */
    /***/
    public void PrintMe()
    {
        /***/
        /* AST NODE TYPE = AST FUNC STMT */
        /***/
        System.out.print("AST NODE FUNC EXP\n");

        /**/
        /* RECURSIVELY PRINT var + func name + exps ... */
        /**/
        if (var != null) var.PrintMe();
        System.out.format("FUNCTION NAME( %s )\n",name);
        if (exps != null) exps.PrintMe();

        /***/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\n_EXP_FUNC(%s)",name));

        /**/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**/
        if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
    }

    public TYPE SemantMe()
    {
        TYPE t = null;
        TYPE varType = null;
        TYPE_LIST params = null;

        /*******************/
        /* [0] return type */
        /*******************/
        if (var != null){
            varType = var.SemantMe();
/*
            try{
                varType = SYMBOL_TABLE.getInstance().find(var.name);
            }
            catch (FindException e){
                this.error();
            }

 */
            if (varType == null) {
                System.out.format(">> ERROR [%d:%d] non existing var type %s\n", 6, 6, varType);
                this.error();
            }
            else {
                if (varType.isClass()){
                    cls = (TYPE_CLASS) varType;
                }
            }
        }
        if (exps != null){
            params = exps.SemantMe();
        }


        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
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


        /*********************************************************/
        /* [6] Return value is irrelevant for class declarations */
        /*********************************************************/
        return ((TYPE_FUNCTION)t).returnType;
    }

    public TEMP IRme()
    {
        TEMP thisInstance = null, t;
        TEMP_LIST paramsTemps = null;
        int numOfParams = 0;
        if (var != null) thisInstance = var.IRme();
        if (exps != null){
            paramsTemps = new TEMP_LIST();
            for (AST_EXP_LIST it = exps ; it != null ; it = it.tail) {
                t = it.head.IRme();
                paramsTemps.addAtFirst(t);
                numOfParams += 4;
            }
        }

        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (var == null){
            if (cls != null && cls.getVtableOffset(name) != -1) { // a class-function was called
                numOfParams += 4;
                IR.getInstance().Add_IRcommand(new IRcommand_Load_This_Instance(dst));
                IR.getInstance().Add_IRcommand(new IRcommand_Virtual_Call_Function_EXP(dst, dst, cls.getVtableOffset(name), paramsTemps));
            } else { // global
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Function_EXP(dst, "func_g_" + name, paramsTemps));
            }
        } else {
            numOfParams += 4;
            IR.getInstance().Add_IRcommand(new IRcommand_Load(dst, thisInstance));
            IR.getInstance().Add_IRcommand(new IRcommand_Virtual_Call_Function_EXP(dst, dst, cls.getVtableOffset(name), paramsTemps));
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Pop_Sp(numOfParams));
        return dst;
    }
}
