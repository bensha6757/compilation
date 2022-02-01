package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Store_Local extends IRcommand{
    Integer offset;
    TEMP dst;

    public IRcommand_Store_Local(String var_name, TEMP dst)
    {
        this.offset = IR.getInstance().getVariableOffset(var_name);
        this.dst = dst;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().storeLocal(offset, this.dst);
    }
}
