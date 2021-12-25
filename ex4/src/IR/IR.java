/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import java.util.HashMap;
import java.util.Map;

/*******************/

public class IR
{
	private IRcommand head=null;
	private IRcommandList tail=null;

    private Map<String, Map<String, Integer>> localVariableToOffset = new HashMap<>();
    private int localVarPointer = 0;

    private Map<String, Map<String, Integer>> argumentsToOffset = new HashMap<>();
    private int argsPointer = -4;

	/******************/
	/* Add IR command */
	/******************/

    public void enterLocalVarToStack(String id) {
        enterLocalVarToStack(id, id);
    }

    public void enterArgToStack(String id) {
        enterArgToStack(id, id);
    }

    public int getVariableOffset(String id) {
        return getVariableOffset(id, id);
    }

    public void enterLocalVarToStack(String id, String field) {
        localVariableToOffset.putIfAbsent(id, new HashMap<>());
        localVariableToOffset.get(id).put(field, localVarPointer);
        localVarPointer += 4;
    }

    public void enterArgToStack(String id, String field) {
        argumentsToOffset.putIfAbsent(id, new HashMap<>());
        argumentsToOffset.get(id).put(field, argsPointer);
        argsPointer -= 4;
    }

    public int getVariableOffset(String id, String field) {
        if (localVariableToOffset.containsKey(id)) {
            return localVariableToOffset.get(id).get(field);
        }
        return argumentsToOffset.get(id).get(field);
    }


    public void clearStack() {
        localVariableToOffset.clear();
        argumentsToOffset.clear();
        localVarPointer = 0;
        argsPointer = -4;
    }

    public void Add_IRcommand(IRcommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
}
