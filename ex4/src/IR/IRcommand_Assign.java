package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Assign extends IRcommand {

    public TEMP t1;
    public TEMP t2;

    public IRcommand_Assign(TEMP t1, TEMP t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().allocate(var_name);
    }
}
