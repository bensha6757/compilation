package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Load_This_Instance extends IRcommand{

    TEMP thisInstance;
    int offset = 0;

    public IRcommand_Load_This_Instance(TEMP thisInstance) {
        this.thisInstance = thisInstance;
        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), thisInstance.getSerialNumber()));
    }

    public IRcommand_Load_This_Instance(TEMP thisInstance, int offset) {
        this(thisInstance);
        this.offset = offset;
    }


    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().loadThisInstance(thisInstance, offset);

    }
}
