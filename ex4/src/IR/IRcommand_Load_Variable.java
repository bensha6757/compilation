package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Load_Variable extends IRcommand
{
    TEMP dst;
    Integer offset;

    public IRcommand_Load_Variable(TEMP dst, String name)
    {
        this.dst = dst;
        this.offset = IR.getInstance().getVariableOffset(name);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().load_address(this.dst, this.offset);
    }
}
