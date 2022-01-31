package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

import java.util.*;

public class IRcommand_Virtual_Call_Function_EXP extends IRcommand
{
    TEMP dst;
    TEMP varTemp;
    int funcOffset;
    TEMP_LIST paramTemps;

    public IRcommand_Virtual_Call_Function_EXP(TEMP dst, TEMP varTemp, int funcOffset, TEMP_LIST paramTemps)
    {
        this.dst = dst;
        this.varTemp = varTemp;
        this.funcOffset = funcOffset;
        this.paramTemps = paramTemps;

        List<Integer> params = new ArrayList<>();
        for(TEMP_LIST param = paramTemps; param != null; param = param.tail){
            params.add(param.head.getSerialNumber());
        }
        params.add(varTemp.getSerialNumber());

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(params, dst.getSerialNumber()));
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().virtualCall(dst, varTemp, funcOffset, paramTemps);
    }
}
