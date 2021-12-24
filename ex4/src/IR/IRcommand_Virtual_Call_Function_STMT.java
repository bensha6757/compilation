package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Virtual_Call_Function_STMT extends IRcommand
{
    TEMP varTemp;
    String funcName;
    TEMP_LIST paramTemps;

    public IRcommand_Virtual_Call_Function_STMT(TEMP varTemp, String funcName, TEMP_LIST paramTemps)
    {
        this.varTemp = varTemp;
        this.funcName = funcName;
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
