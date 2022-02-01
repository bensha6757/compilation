package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Restore_Sp_From_S0 extends IRcommand{
    public IRcommand_Restore_Sp_From_S0()
    {
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().restoreSpFromS0();
    }

}
