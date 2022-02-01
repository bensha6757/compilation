package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.ArrayList;
import java.util.List;

public class IRcommand_Call_Function_EXP extends IRcommand
{
    TEMP dst;
    String funcName;
    TEMP_LIST paramTemps;

    public IRcommand_Call_Function_EXP(TEMP dst, String funcName, TEMP_LIST paramTemps)
    {
        this.dst = dst;
        this.funcName = funcName;
        this.paramTemps = paramTemps;

        List<Integer> willLive = new ArrayList<>();
        for (TEMP_LIST tmp = paramTemps ; tmp != null ; tmp = tmp.tail) {
            willLive.add(tmp.head.getSerialNumber());
        }
        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(willLive, dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().funcGlobalCall(dst, funcName, paramTemps);
    }
}
