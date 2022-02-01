package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Store_Local extends IRcommand{
    Integer offset;
    TEMP dst;

    public IRcommand_Store_Local(String var_name, TEMP dst)
    {
        this.offset = IR.getInstance().getVariableOffset(var_name);
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().storeLocal(offset, this.dst);
    }
}
