package AST;

import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_FUNCDEC_STMT extends AST_FUNCDEC {


    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_FUNCDEC_STMT(AST_TYPE type, String funcName, AST_TYPEID_LIST typeIds, AST_STMT_LIST stmts)
    {
        System.out.print("---AST_FUNCDEC_STMT---");
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.format("====================== funcDec -> type ID( %s )(typeIds) {stmts}\n", funcName);

        /***********/
        /* COPY INPUT DATA MEMBERS ... */
        /***********/
        this.type = type;
        this.funcName = funcName;
        this.typeIds = typeIds;
        this.stmts = stmts;
    }

    /*****************/
    /* The printing message for class dec func AST node */
    /*****************/
    public void PrintMe()
    {
        /*************/
        /* AST NODE TYPE = AST FUNCDEC STMT */
        /*************/
        System.out.print("AST NODE FUNCDEC STMT\n");

        /**************/
        /* RECURSIVELY PRINT type + funcName + typeIds + stmts ... */
        /**************/
        if (type != null) type.PrintMe();
        System.out.format("FUNC NAME( %s )\n", funcName);
        if (typeIds != null) typeIds.PrintMe();
        if (stmts != null) stmts.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
            SerialNumber,
            String.format("AST\nFUNCDEC\nSTMT\n(%s)",funcName));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (typeIds != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,typeIds.SerialNumber);
        if (stmts != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmts.SerialNumber);
    }

    public TYPE SemantMe()
    {
        TYPE t = null, t2 = null;
        TYPE returnType = null;
        TYPE_LIST type_list = null, head = null;

        /*******************/
        /* [0] return type */
        /*******************/
        try{
            returnType = SYMBOL_TABLE.getInstance().find(type.typeName);
        }
        catch (FindException e){
            System.out.format(">> ERROR [%d:%d] non existing return type \n",6,6);
            this.error();
        }
        if (returnType == null)
        {
            System.out.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,returnType);
            this.error();
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().checkIfVarExistsInCurrentScope(funcName))
        {
            System.out.format(">> ERROR [%d:%d] function %s already exists in scope\n",2,2,funcName);
            this.error();
        }

        try {
            TYPE_LIST params = null;
            if (typeIds != null){
                params = typeIds.SemantMe();
            }
            SYMBOL_TABLE.getInstance().find(funcName, returnType.name, params, true);
        } catch (FindException e) {
            System.out.format(">> ERROR [%d:%d] illegal override function %s\n",2,2,funcName);
            this.error();
        }

        /****************************/
        /* [1] Begin Function Scope */
        /****************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Input Params */
        /***************************/

        for (AST_TYPEID_LIST param = typeIds; param  != null; param = param.tail)
        {
            try{
                t = SYMBOL_TABLE.getInstance().find(param.head.typeName);
            }
            catch (FindException e){
                this.error();
            }
            if (t == null)
            {
                System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,param.head.typeName);
                this.error();
            }
            else
            {
                if (type_list == null){
                    type_list = new TYPE_LIST(t, param.name, null);
                    head = type_list;
                } else {
                    type_list.tail = new TYPE_LIST(t, param.name, null);
                    type_list = type_list.tail;
                }
                SYMBOL_TABLE.getInstance().enter(param.name,t);
            }
        }
        type_list = head;

        /*******************/
        /* [3] Semant Body */
        /*******************/
        t2 = stmts.SemantMe();
        System.out.println(this.funcName);
        System.out.println(this.type.typeName);
        if (((returnType.isVoid() && t2 != null)) || ((returnType != t2) && (t2 != null))){
            System.out.println("$$$$ " + t2.name);
            System.out.format(">> ERROR [%d:%d] type mismatch for return statement\n",6,6);
            this.error();
        }

        /*****************/
        /* [4] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(funcName, new TYPE_FUNCTION(returnType, funcName, type_list));

        /*********************************************************/
        /* [6] Return value is irrelevant for class declarations */
        /*********************************************************/
        return returnType;
    }

    @Override
    public void SemantMe(TYPE_CLASS cls) {
        TYPE func_type = this.SemantMe();
        if (cls != null){
            cls.addMember(func_type, funcName);
        }
    }

}
