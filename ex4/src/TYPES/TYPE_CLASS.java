package TYPES;


import com.sun.xml.internal.ws.util.StringUtils;

import java.util.*;

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

    private List<List<String>> vtable;

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
        vtable = new ArrayList<>();
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

    public int getVtableOffset(String funcName){
        for (int i = 0 ; i < vtable.size() ; i++) {
            List<String> function = vtable.get(i);
            if (funcName.equals(function.get(0)))
                return 4 * i;
        }
        return -1;
    }


    public void crateVtable() {
        String funcName, dec;
        if (father != null) {
            for (List<String> fatherFunc : father.vtable) {
                funcName = fatherFunc.get(0);
                dec = fatherFunc.get(1);
                if (isFuncReDefinedInSon(funcName)) {
                    vtable.add(Arrays.asList(funcName, dec, name));
                } else {
                    vtable.add(fatherFunc);
                }
            }
        }

        for (TYPE_LIST member = data_members ; member != null ; member = member.tail) {
            if (member.head.isFunction() && !isFuncInVtable(member.head.name)) {
                vtable.add(Arrays.asList(member.head.name, name, name));
            }
        }
    }

    private boolean isFuncInVtable(String funcName) {
        for (List<String> functions : vtable) {
            if (funcName.equals(functions.get(0)))
                return true;
        }
        return false;
    }

    private boolean isFuncReDefinedInSon(String funcName) {
        if (data_members != null){
            for (TYPE_LIST member = data_members ; member != null ; member = member.tail) {
                if (member.head.isFunction() && member.head.name.equals(funcName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
