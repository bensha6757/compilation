package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Function_EXP extends IRcommand
{
    TEMP dst;
    int funcOffset;
    TEMP_LIST paramTemps;

    public IRcommand_Call_Function_EXP(TEMP dst, int funcOffset, TEMP_LIST paramTemps)
    {
        this.dst = dst;
        this.funcOffset = funcOffset;
        this.paramTemps = paramTemps;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().beqz(t,label_name);
    }
}
