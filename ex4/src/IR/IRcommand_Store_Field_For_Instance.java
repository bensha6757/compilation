package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.Collections;

public class IRcommand_Store_Field_For_Instance extends IRcommand{
    int offset;
    TEMP value;
    TEMP instance;

    public IRcommand_Store_Field_For_Instance(int offset, TEMP value, TEMP instance) {
        this.value = value;
        this.instance = instance;
        this.offset = offset;

        // offset(instance) = value
        Register_Allocation.getInstance().addCommandToCFG(new IR_Node(Arrays.asList(value.getSerialNumber(), instance.getSerialNumber()), instance.getSerialNumber()));
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().storeFieldForInstance(offset, instance, value);
    }
}
