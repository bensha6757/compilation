package IR;

import MIPS.MIPSGenerator;

import java.util.List;

public class IRcommand_Allocate_Vtable extends IRcommand{

    String className;
    List<List<String>> vtable;
    public IRcommand_Allocate_Vtable(List<List<String>> vtable, String className) {
        this.vtable = vtable;
        this.className = className;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().createVtable(vtable, className);
    }
}
