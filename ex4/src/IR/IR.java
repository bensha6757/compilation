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
	// 1/22
	//  maybe we need current function field
	private IRcommand head=null;
	private IRcommandList tail=null;

    private Map<String, Integer> localVariableToOffset = new HashMap<>();
    private int localVarPointer = 0;

    private Map<String, Integer> argumentsToOffset = new HashMap<>();
    private int argsPointer = -8;

	/******************/
	/* Add IR command */
	/******************/

    public void enterLocalVarToStack(String id) {
        localVariableToOffset.putIfAbsent(id, localVarPointer);
		localVarPointer += 4;
    }

    public void enterArgToStack(String id) {
        argumentsToOffset.putIfAbsent(id, argsPointer);
        argsPointer -= 4;
    }

    public int getVariableOffset(String id) {
        if (localVariableToOffset.containsKey(id)) {
            return localVariableToOffset.get(id);
        }
        return argumentsToOffset.get(id);
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
