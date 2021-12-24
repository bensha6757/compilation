package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

public class IRcommand_Virtual_Call_Function_EXP extends IRcommand
{
    TEMP dst;
    TEMP varTemp;
    String funcName;
    TEMP_LIST paramTemps;

    public IRcommand_Virtual_Call_Function_EXP(TEMP dst, TEMP varTemp, String funcName, TEMP_LIST paramTemps)
    {
        this.dst = dst;
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
