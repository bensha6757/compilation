package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_NIL extends IRcommand{

    TEMP dst;
    public IRcommand_NIL(TEMP dst) {
         this.dst = dst;

         Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), dst.getSerialNumber()));
    }
    @Override
    public void MIPSme()
    {
        MIPSGenerator.getInstance().li(dst,0);
    }
}
