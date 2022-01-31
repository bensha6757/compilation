package IR;
import MIPS.*;

public class IRcommand_Function_Epilogue extends IRcommand{

    public IRcommand_Function_Epilogue()
    {
    }

    /*****/
    /* MIPS me !!! */
    /*****/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().funcDecEpilogue();
    }
}
