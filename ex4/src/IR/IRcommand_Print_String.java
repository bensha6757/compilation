package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Print_String extends IRcommand{

    TEMP strTmp;
    public IRcommand_Print_String(TEMP strTmp) {
        this.strTmp = strTmp;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().print_string(strTmp);
    }
}
