package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_Call_Function_STMT extends IRcommand
{
    String funcName;
    TEMP_LIST paramTemps;

    public IRcommand_Call_Function_STMT(String funcName, TEMP_LIST paramTemps)
    {
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
