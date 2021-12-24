package TYPES;

import SYMBOL_TABLE.SYMBOL_TABLE;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return this instanceof TYPE_CLASS;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return this instanceof TYPE_ARRAY;}

    public boolean isArrayRight(){ return this instanceof TYPE_ARRAY_RIGHT;}

    public boolean isFunction(){ return this instanceof TYPE_FUNCTION;}

    public boolean isInt(){ return this == TYPE_INT.getInstance();}

    public boolean isString(){ return this == TYPE_STRING.getInstance();}

    public boolean isVoid(){ return this == TYPE_VOID.getInstance();}

    public boolean isNil(){ return this == TYPE_NIL.getInstance();}

    public boolean isLegalAssignment(TYPE t2){
        if (this.isClass() && t2.isClass()){
            for (TYPE_CLASS father = (TYPE_CLASS)t2; father != null ; father = father.father){
                if (father.name.equals(this.name)){
                    return true;
                }
            }
            return false;
        }

        if ((this.isClass() || this.isArray()) && t2.isNil()){
            return true;
        }

        if ((this.isArray() && t2.isArrayRight())){
            TYPE_ARRAY leftArray = (TYPE_ARRAY) this;
            TYPE_ARRAY_RIGHT rightArray = ((TYPE_ARRAY_RIGHT) t2);
            return (leftArray.type == rightArray.type) || (SYMBOL_TABLE.getInstance().isFather(rightArray.type, leftArray.type));
        }

        return (!this.isArray() || !t2.isArray()) && this == t2;
    }
}
