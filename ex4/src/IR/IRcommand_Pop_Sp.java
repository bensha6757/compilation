package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Pop_Sp extends IRcommand {

    int numOfParams;
    public IRcommand_Pop_Sp(int numOfParams) {
        this.numOfParams = numOfParams;
    }

    @Override
    public void MIPSme()
    {
        MIPSGenerator.getInstance().popStack(numOfParams);
    }
}
