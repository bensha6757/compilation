package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Store_Local extends IRcommand{
    Integer offset;
    TEMP src;

    public IRcommand_Store_Local(String var_name, TEMP src)
    {
        this.offset = IR.getInstance().getVariableOffset(var_name);
        this.src = src;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().store_local(offset, this.src);
    }
}
