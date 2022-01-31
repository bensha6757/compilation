package TEMP;

public class TEMP_LIST{
    public TEMP head;
    public TEMP_LIST tail;

    public TEMP_LIST(TEMP head, TEMP_LIST tail)
    {
        this.head = head;
        this.tail = tail;
    }

    public TEMP_LIST()
    {
    }

    public void addAtFirst(TEMP temp) {
        if (head != null) {
            tail = new TEMP_LIST(head, tail);
        }
        head = temp;
    }

    public void addAtEnd(TEMP tmp){
        if ((head == null) && (tail == null))
        {
            this.head = tmp;
        }
        else if ((head != null) && (tail == null))
        {
            this.tail = new TEMP_LIST(tmp,null);
        }
        else
        {
            TEMP_LIST it = tail;
            while ((it != null) && (it.tail != null))
            {
                it = it.tail;
            }
            it.tail = new TEMP_LIST(tmp,null);
        }

    }
}
