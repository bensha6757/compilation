package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Print_String extends IRcommand{

    TEMP strTmp;
    public IRcommand_Print_String(TEMP strTmp) {
        this.strTmp = strTmp;

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Collections.emptyList(), strTmp.getSerialNumber()));

    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().print_string(strTmp);
    }
}
