package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_New_Array extends IRcommand
{
    public TEMP exp_temp;
    public TEMP dest;

    public IRcommand_New_Array(TEMP exp_temp, TEMP dest)
    {
        this.exp_temp = exp_temp;
        this. dest = dest;

    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {

    }
}
