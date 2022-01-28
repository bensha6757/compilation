package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Virtual_Call_Function_STMT extends IRcommand
{
    TEMP varTemp;
    int funcOffset;
    TEMP_LIST paramTemps;

    public IRcommand_Virtual_Call_Function_STMT(TEMP varTemp, int funcOffset, TEMP_LIST paramTemps)
    {
        this.varTemp = varTemp;
        this.funcOffset = funcOffset;
        this.paramTemps = paramTemps;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().virtualCall(varTemp, funcOffset, paramTemps);
    }
}
