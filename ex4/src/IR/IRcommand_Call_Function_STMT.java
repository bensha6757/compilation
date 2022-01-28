package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Function_STMT extends IRcommand
{
    int funcOffset;
    TEMP_LIST paramTemps;

    public IRcommand_Call_Function_STMT(int funcOffset, TEMP_LIST paramTemps)
    {
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
