/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************/

public class IR
{
	private IRcommand head = null;
	private IRcommandList tail = null;

    private Map<String, Integer> localVariableToOffset = new HashMap<>();
    private int localVarPointer = -44;

    private Map<String, Integer> argumentsToOffset = new HashMap<>();
    private int argsPointer = 8;

    private String currFuncName = null;

    // true == global string
    private Map<String, Boolean> globalsToIsString;

	/******************/
	/* Add IR command */
	/******************/

    public void enterLocalVarToStack(String id) {
        localVariableToOffset.putIfAbsent(id, localVarPointer);
		localVarPointer -= 4;
    }

    public boolean isLocalVarExists(String id) {
        return localVariableToOffset.containsKey(id) || argumentsToOffset.containsKey(id);
    }


    public void enterArgToStack(String id) {
        argumentsToOffset.putIfAbsent(id, argsPointer);
        argsPointer += 4;
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
        localVarPointer = -44;
        argsPointer = 8;
    }

    public Map<String, Boolean> getGlobals() {
        return globalsToIsString;
    }

    public void addGlobal(String s, boolean isString)
    {
        globalsToIsString.put(s, isString);
    }



    public void setCurrentFunction(String funcName){
        this.currFuncName = funcName;
    }

    public String getCurrentFunction(){
        return this.currFuncName;
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
