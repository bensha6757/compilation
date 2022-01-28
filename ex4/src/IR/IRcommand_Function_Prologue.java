package IR;

import MIPS.*;

public class IRcommand_Function_Prologue  extends IRcommand{
    int numOfLocalVars;

    public IRcommand_Function_Prologue(int numOfLocalVars)
    {
         this.numOfLocalVars = numOfLocalVars;
    }

    /*****/
    /* MIPS me !!! */
    /*****/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().funcDecPrologue(numOfLocalVars);
    }


}
