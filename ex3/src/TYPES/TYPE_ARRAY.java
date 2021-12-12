package TYPES;

public class TYPE_ARRAY extends TYPE {
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE type;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(String name, TYPE type)
    {
        this.name = name;
        this.type = type;
    }
}
