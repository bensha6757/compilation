package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Load_Global_Variable extends IRcommand{
    TEMP dst;
    String name;

    public IRcommand_Load_Global_Variable(TEMP dst, String name)
    {
        this.dst = dst;
        this.name = name;

        Register_Allocation.getInstance().addCommandToCFG(
            new IR_Node(Collections.emptyList(),
                dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().loadGlobalVar(dst, name);
    }
}
