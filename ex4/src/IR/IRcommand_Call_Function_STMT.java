package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRcommand_Call_Function_STMT extends IRcommand
{
    String funcName;
    TEMP_LIST paramTemps;

    public IRcommand_Call_Function_STMT(String funcName, TEMP_LIST paramTemps)
    {
        this.funcName = funcName;
        this.paramTemps = paramTemps;

        List<Integer> willLive = new ArrayList<>();
        for (TEMP_LIST tmp = paramTemps ; tmp != null ; tmp = tmp.tail) {
            willLive.add(tmp.head.getSerialNumber());
        }
        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(willLive, -1));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().funcGlobalCall(null, funcName, paramTemps);
    }
}
