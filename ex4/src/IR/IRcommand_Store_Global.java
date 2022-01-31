package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Store_Global extends IRcommand{

    String globalName;
    TEMP value;

    public IRcommand_Store_Global(String globalName, TEMP value) {
        this.globalName = globalName;
        this.value = value;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.singletonList(value.getSerialNumber()), -1));
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().storeGlobal(globalName, value);
    }
}
