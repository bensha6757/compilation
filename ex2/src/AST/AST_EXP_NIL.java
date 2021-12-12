package AST;

public class AST_EXP_NIL extends AST_EXP{

    public AST_EXP_NIL(){
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }
    public void PrintMe()
    {
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NIL");
    }
}
