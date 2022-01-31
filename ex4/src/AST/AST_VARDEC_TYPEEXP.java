package AST;

import IR.*;
import SYMBOL_TABLE.FindException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_VARDEC_TYPEEXP extends AST_VARDEC {
    /************************/
    /* simple variable name */
    /************************/
    TYPE_CLASS cls;
    TYPE varType;
    boolean isGlobal = false;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_VARDEC_TYPEEXP(AST_TYPE t, String varName, AST_EXP exp)
    {
        System.out.println("---AST_VARDEC_TYPEEXP---");
        System.out.println(exp);

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== varDec ->  type ID( %s ) [ ASSIGN exp ];\n",varName);
        System.out.format("====================== varDec ->  type ID( %s ) ASSIGN newExp;\n",varName);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.varName = varName;
        this.type = t;
        this.exp = exp;
    }

    /*****************************************************/
    /* The printing message for a varDec TYPEEXP AST node */
    /*****************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST varDec TYPEEXP */
        /*************************************/
        System.out.print("AST NODE varDec TYPEEXP\n");

        /****************************************/
        /* PRINT NAME+ TYPE + EXP ... */
        /****************************************/
        if (type != null) type.PrintMe();
        System.out.format("AST NODE SIMPLE VAR( %s )\n",varName);
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("AST\nVARDEC\nTYPEEXP\n(%s)",varName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe()
    {

        TYPE t1 = null, t2;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        try{
            t1 = SYMBOL_TABLE.getInstance().find(type.typeName);
        }
        catch (FindException e){
            this.error();
        }
        if ((t1 == null) || t1.isVoid())
        {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type.typeName);
            this.error();
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().checkIfVarExistsInCurrentScope(varName))
        {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,varName);
            this.error();
        }

        try{
            if (SYMBOL_TABLE.getInstance().checkVariableShadowing(varName, t1))
            {
                System.out.format(">> ERROR [%d:%d] variable %s already exists in father class\n",2,2,varName);
                this.error();
            }
        } catch (FindException e){
            System.out.format(">> ERROR [%d:%d] variable %s already exists in father class as function\n",2,2,varName);
            this.error();
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(varName,t1);
        varType = t1;
        isGlobal = SYMBOL_TABLE.getInstance().checkIfGlobal();

        if (exp != null){
            t2 = exp.SemantMe();
            if (!SYMBOL_TABLE.getInstance().isInClassScope()) {
                if (!t1.isLegalAssignment(t2)){
                    System.out.format(">> ERROR [%d:%d] illegal assignment \n",2,2);
                    this.error();
                }
            }
            else if (!((t1.isInt() && (exp instanceof AST_EXP_INT)) ||
                (t1.isString() && (exp instanceof AST_EXP_STRING)) ||
                (!t1.isInt() && !t1.isString() && (exp instanceof AST_EXP_NIL)))){
                System.out.format(">> ERROR [%d:%d] illegal initialization inside a class - not a constant variable \n",2,2);
                this.error();
            }
        }


        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return t1;
    }

    @Override
    public void SemantMe(TYPE_CLASS cls) {
        TYPE var_type = this.SemantMe();
        if (cls != null){
            cls.addMember(var_type, varName);
        }
        this.cls = cls;
        this.varType = var_type;
        this.isGlobal = false;
    }

    private void getExpDefaultValue() {
        if (exp == null){
            if (varType.isInt()) {
                // 0
                this.exp = new AST_EXP_INT(0);
            } else if (varType.isString()){
                // ""
                this.exp = new AST_EXP_STRING("");
            } else {
                // NIL
                this.exp = new AST_EXP_NIL();
            }
        }
    }

    @Override
    public TEMP IRme() { // not a field
        if (isGlobal) {
            IR.getInstance().addGlobal(varName, varType.isString());
            if (exp == null) {
                getExpDefaultValue();
            }
            TEMP value = exp.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(this.varName, value));
        } else {
            IR.getInstance().enterLocalVarToStack(this.varName);
            if (this.exp != null) {
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(this.varName, this.exp.IRme()));
            }
        }
        return null;
    }


    @Override
    public TEMP IRme(TEMP thisInstance) { // for fields
        if (exp == null)
            getExpDefaultValue();
        TEMP value = exp.IRme();
        if (cls != null) {
            int offset = cls.getFieldOffset(varName);
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Field_For_Instance(offset, value, thisInstance));
        }
        return null;
    }

}
