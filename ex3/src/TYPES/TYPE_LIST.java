package TYPES;

import AST.AST_TYPE;
import AST.AST_TYPEID_LIST;

public class TYPE_LIST extends TYPE
{
	/****************/
	/* DATA MEMBERS */
	/****************/
    public TYPE head;
    public String name;
    public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head, String name, TYPE_LIST tail)
	{
		this.head = head;
        this.name = name;
		this.tail = tail;
	}
}
