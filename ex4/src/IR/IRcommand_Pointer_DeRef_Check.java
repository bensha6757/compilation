package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Collections;

public class IRcommand_Pointer_DeRef_Check extends IRcommand{
    TEMP var;

    public IRcommand_Pointer_DeRef_Check(TEMP var)
    {
        this.var = var;

        Register_Allocation.getInstance().addCommandToCFG(
            new IR_Node(Collections.singletonList(var.getSerialNumber()), -1));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().pointerDereferenceCheck(var);
    }

}
