/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;


/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 100;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;
    public TYPE_CLASS curFather;
    public int scopeCount = 0;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{

        return (s.hashCode() & 0x7fffffff) % table.length;
/*
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
*/
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name,TYPE t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);
		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++, scopeCount);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/

    // a.foo(15);
    // a.var = new Ronen;
    public TYPE find(String name, TYPE_LIST params, boolean isFunction, TYPE_CLASS varType) throws FindException {
        if (name == null) return null;
        return findInParents(name, null, params, varType, isFunction);
    }


    public boolean compareBetweenFunctions(boolean isFunction, TYPE type, String returnTypeName, TYPE_LIST params){
        if (isFunction && type.isFunction()){
            if (!checkIfFuncIdentical(returnTypeName, params, (TYPE_FUNCTION) type)){
                System.out.println("Error not identical");
                return false;
            }
        } else if (isFunction != type.isFunction()){
            System.out.println("Error not both funcs");
            return false;
        }
        return true;
    }

    // int foo(int i)
    public TYPE find(String name, String returnTypeName, TYPE_LIST params, boolean isFunction) throws FindException {
        if (name == null) return null;
        SYMBOL_TABLE_ENTRY e, e2;

        for (e = table[hash(name)]; e != null && !e.checkIfVarIsGlobal(); e = e.next)
        {
            if (name.equals(e.name)) {
                //return compareBetweenFunctions(isFunction, e.type, returnTypeName, params) ? e.type : "ERROR";
				if(compareBetweenFunctions(isFunction, e.type, returnTypeName, params)){
					return e.type;
				}
				else{
					throw new FindException("Illegal override or non exist function");
				}
            }
        }


        TYPE t = findInParents(name, returnTypeName, params, curFather, isFunction);
        if (t != null){
            return t;
        }
        // for foo(5)
        if (returnTypeName == null || returnTypeName.equals("")){
            for (e2 = e; e2 != null; e2 = e2.next)
            {
                if (name.equals(e2.name)) {
                    //return compareBetweenFunctions(isFunction, e.type, returnTypeName, params) ? e.type : "ERROR";
                    if(compareBetweenFunctions(isFunction, e2.type, returnTypeName, params)){
                        return e2.type;
                    }
                    else {
                        throw new FindException("Illegal override or non exist function 2");
                    }
                }
            }
        }

        return null;
    }

    // var = 5;
    public TYPE find(String name) throws FindException {
        return find(name, "", null, false);
	}

    private TYPE findInParents(String name, String returnTypeName, TYPE_LIST params, TYPE_CLASS father, boolean isFunction) throws FindException {
        for (TYPE_CLASS fatherClass = father ; fatherClass != null ; fatherClass = fatherClass.father){
            for (TYPE_LIST member = fatherClass.data_members ; member != null ; member = member.tail){
                if (member.name.equals(name)){
                    //return compareBetweenFunctions(isFunction, member.head, returnTypeName, params) ? member.head : "Error";
					if(compareBetweenFunctions(isFunction, member.head, returnTypeName, params)){
						return member.head;
					}
					else{
						throw new FindException("Illegal override or non exist function 2");
					}
                }
            }
        }
        return null;
    }

    private boolean checkIfFuncIdentical(String returnTypeName, TYPE_LIST params, TYPE_FUNCTION fatherFunc){
        if ((returnTypeName == null) || returnTypeName.equals(fatherFunc.returnType.name)){
			TYPE_LIST fatherFuncParams = fatherFunc.params;
            while (params != null && fatherFuncParams != null){
				if((!fatherFuncParams.head.isInt() && !fatherFuncParams.head.isString() && params.head.isNil()) ||
                    fatherFuncParams.head.name.equals(params.head.name) ||
                    (isFather(params.head, fatherFuncParams.head))){
					fatherFuncParams = fatherFuncParams.tail;
					params = params.tail;
				}
                else{
                    return false;
                }
            }
            return params == null && fatherFuncParams == null;
        }
		return false;
    }

    public boolean isFather(TYPE son, TYPE father){
        if (son != null && son.isClass() && father!=null && father.isClass()){
            for (TYPE_CLASS fatherClass = ((TYPE_CLASS) son).father ; fatherClass != null ; fatherClass = fatherClass.father){
                if (fatherClass.name.equals(father.name)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIfGlobal(){
        return scopeCount == 0;
        /*
        SYMBOL_TABLE_ENTRY curTop = top;
        while (curTop != null && curTop.name != "SCOPE-BOUNDARY")
        {
            curTop = curTop.prevtop;
        }
        return curTop == null;
        */
    }

    public boolean isInClassScope(){
        SYMBOL_TABLE_ENTRY curTop = top;
        while (curTop!= null && !(curTop.name.equals("SCOPE-BOUNDARY")))
        {
            curTop = curTop.prevtop;
        }
        if (curTop == null)
            return false;
        return curTop.type.name.equals("class");
    }

    public TYPE_FUNCTION getMyFunc(){
        SYMBOL_TABLE_ENTRY curTop = top;
        while (!(curTop.name.equals("SCOPE-BOUNDARY")) || !curTop.type.name.equals("function"))
        {
            curTop = curTop.prevtop;
        }
        curTop = curTop.prevtop;
        return (TYPE_FUNCTION)(curTop.type);
    }


    public boolean checkVariableShadowing(String varName, TYPE varType) throws FindException {
        TYPE t = SYMBOL_TABLE.getInstance().findInParents(varName, null, null, curFather, false);
        if (t == null)
            return false;
        return !t.name.equals(varType.name);
    }

    public boolean checkIfVarExistsInCurrentScope(String var){
        SYMBOL_TABLE_ENTRY curTop = top;
        while (curTop != null && !(curTop.name.equals("SCOPE-BOUNDARY")))
        {
            if (curTop.name.equals(var)){
                return true;
            }
            curTop = curTop.prevtop;
        }
        return false;
    }


    /***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope(String name)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be ablt to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
        scopeCount++;
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES(name));

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
        scopeCount--;
	}
	
	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();
			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());
            instance.enter("nil", TYPE_NIL.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						TYPE_INT.getInstance(),
                        "int",
						null)));

            instance.enter(
                "PrintString",
                new TYPE_FUNCTION(
                    TYPE_VOID.getInstance(),
                    "PrintString",
                    new TYPE_LIST(
                        TYPE_STRING.getInstance(),
                        "string",
                        null)));

            instance.enter(
                "PrintTrace",
                new TYPE_FUNCTION(
                    TYPE_VOID.getInstance(),
                    "PrintTrace",
                    new TYPE_LIST(
                        null,
                        "",
                        null)));
        }
		return instance;
	}
}
