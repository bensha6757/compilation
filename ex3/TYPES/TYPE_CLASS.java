package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;

    private TYPE_LIST data_members_end;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
        this.data_members_end = data_members;
        if (data_members != null){
            while (data_members_end.tail != null){
                data_members_end = data_members_end.tail;
            }
        }
	}

    public void addMember(TYPE member, String name){
        TYPE_LIST node = new TYPE_LIST(member, name, null);
        if (data_members == null){
            data_members = node;
            data_members_end = data_members;
        } else {
            data_members_end.tail = node;
            data_members_end = data_members_end.tail;
        }
    }
}
