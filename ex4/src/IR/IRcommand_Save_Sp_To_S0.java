package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Save_Sp_To_S0 extends IRcommand{
    public IRcommand_Save_Sp_To_S0()
    {
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().saveSpToS0();
    }

}
