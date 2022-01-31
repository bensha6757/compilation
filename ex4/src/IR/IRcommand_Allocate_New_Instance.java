package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Allocate_New_Instance extends IRcommand{
    TEMP newInstance;
    int classSize;
    String vtLabel;

    public IRcommand_Allocate_New_Instance(TEMP newInstance, int classSize, String vtLabel) {
        this.newInstance = newInstance;
        this.classSize = classSize;
        this.vtLabel = vtLabel;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), newInstance.getSerialNumber()));
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().allocateNewInstance(classSize, newInstance, vtLabel);
    }
}
