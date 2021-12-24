package TYPES;

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
            return ((TYPE_ARRAY) this).type == ((TYPE_ARRAY_RIGHT) t2).type;
        }

        return (!this.isArray() || !t2.isArray()) && this == t2;
    }
}
