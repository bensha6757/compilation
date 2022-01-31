package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Push_Function_Param_To_Stack extends IRcommand{

    TEMP paramTemp;
    public IRcommand_Push_Function_Param_To_Stack(TEMP paramTemp) {
        this.paramTemp = paramTemp;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().pushFunctionParamToStack(paramTemp);
    }
}
