package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IRcommand_Virtual_Call_Function_STMT extends IRcommand
{
    TEMP varTemp;
    int funcOffset;
    TEMP_LIST paramTemps;

    public IRcommand_Virtual_Call_Function_STMT(TEMP varTemp, int funcOffset, TEMP_LIST paramTemps)
    {
        this.varTemp = varTemp;
        this.funcOffset = funcOffset;
        this.paramTemps = paramTemps;
        List<Integer> params = new ArrayList<>();
        for(TEMP_LIST param = paramTemps; param != null; param = param.tail){
            params.add(param.head.getSerialNumber());
        }
        params.add(varTemp.getSerialNumber());

        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(params, -1));

    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().virtualCall(null, varTemp, funcOffset, paramTemps);
    }
}
