package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Load_Variable extends IRcommand
{
    TEMP dst;
    Integer offset;

    public IRcommand_Load_Variable(TEMP dst, String name)
    {
        this.dst = dst;
        this.offset = IR.getInstance().getVariableOffset(name);
        Register_Allocation.getInstance().addCommandToCFG(
                new IR_Node(Collections.emptyList(),
                        dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().loadVariable(this.dst, this.offset);
    }
}
