/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.IR;
import IR.IRcommand;
import IR.Register_Allocation;
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;
    
    private Map<String, String> labels = new HashMap<>();

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
        String user_main = IRcommand.getFreshLabel("user_main");

        fileWriter.format("%s:\n", user_main);
        funcDecPrologue(0);
        fileWriter.print("\tjal func_g_main\n");
        funcDecEpilogue();

        fileWriter.print("main:\n");
        fileWriter.format("\tjal %s\n", user_main);
        fileWriter.print("\tla $t0, new_line\n");
        printErrorMessage();
        fileWriter.print("\tli $v0,10\n");
        fileWriter.print("\tsyscall\n");

        fileWriter.close();

    }
	public void print_int(TEMP t)
	{
		int idx=t.getRealSerialNumber();
		fileWriter.format("\tmove $a0,$t%d\n",idx);
        fileWriter.print("\tli $v0, 1\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.print("\tla $a0, space\n");
        fileWriter.print("\tli $v0, 4\n");
        fileWriter.print("\tsyscall\n");
	}

    public void print_string(TEMP strTmp) {
        fileWriter.format("\tmove $a0, $t%d\n", strTmp.getRealSerialNumber());
        fileWriter.print("\tli $v0, 4\n");
        fileWriter.print("\tsyscall\n");
        // fileWriter.print("\tjr $ra\n");
    }

    public void printErrorMessage() {
        fileWriter.format("\tmove $a0, $t0\n");
        fileWriter.print("\tli $v0, 4\n");
        fileWriter.print("\tsyscall\n");
        // fileWriter.print("\tjr $ra\n");
    }


    public void Malloc() {
        String Malloc = IRcommand.getFreshLabel("Malloc");
        labels.put("Malloc", Malloc);
        fileWriter.format("%s:\n", Malloc);
        fileWriter.print("\tlw $a0, 0($sp)\n");
        fileWriter.print("\tli $v0, 9\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.print("\tjr $ra\n");
    }

    public void Exit() {
        String Exit = IRcommand.getFreshLabel("Exit");
        labels.put("Exit", Exit);
        fileWriter.format("%s:\n", Exit);
        fileWriter.print("\tli $v0, 10\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.print("\tjr $ra\n");
    }

    public void invalid_pointer_dereference() {
        String invalid_pointer_dereference = IRcommand.getFreshLabel("invalid_pointer_dereference");
        labels.put("invalid_pointer_dereference", invalid_pointer_dereference);
        fileWriter.format("%s:\n", invalid_pointer_dereference);

        fileWriter.print("\tla $t0, string_invalid_ptr_dref\n");

        printErrorMessage();

        fileWriter.format("\tjal %s\n", labels.get("Exit"));
    }

    public void division_by_zero() {
        String division_by_zero = IRcommand.getFreshLabel("division_by_zero");
        labels.put("division_by_zero", division_by_zero);
        fileWriter.format("%s:\n", division_by_zero);
        fileWriter.print("\tla $t0, string_illegal_div_by_0\n");

        printErrorMessage();

        fileWriter.format("\tjal %s\n", labels.get("Exit"));
    }


    public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tg_%s: .word 721\n",var_name);
	}
    public void pointerDereferenceCheck(TEMP varTmp){
        int varIdx = varTmp.getRealSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, %s\n", varIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.flush();
    }
	public void load(TEMP dst, TEMP varTmp)
	{
        int dstIdx = dst.getRealSerialNumber();
        int varIdx = varTmp.getRealSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, %s\n", varIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tlw $t%d, 0($t%d)\n",dstIdx, varIdx);
        fileWriter.flush();
	}
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getRealSerialNumber();
		fileWriter.format("\tsw $t%d,g_%s\n",idxsrc,var_name);
	}
	public void li(TEMP t,int value)
	{
		int idx=t.getRealSerialNumber();
		fileWriter.format("\tli $t%d,%d\n",idx,value);
	}
    public void checkOverFlow(TEMP dst) {
        int dstIdx = dst.getRealSerialNumber();
        String overflowLabel = IRcommand.getFreshLabel("overflow");
        String endLabel = IRcommand.getFreshLabel("end");

        fileWriter.format("\tli $s2, 32767\n");
        bgt(dst, overflowLabel, 2);

        fileWriter.format("\tli $s2, -32768\n");
        blt(dst, overflowLabel, 2);

        fileWriter.format("\tj %s\n", endLabel);

        fileWriter.format("%s:\n", overflowLabel);
        fileWriter.format("\tmove $t%d, $s2\n", dstIdx);

        fileWriter.format("%s:\n", endLabel);

        fileWriter.flush();
    }
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();
		int dstidx=dst.getRealSerialNumber();

		fileWriter.format("\taddu $t%d,$t%d,$t%d\n",dstidx, i1, i2);
        checkOverFlow(dst);
	}
    public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
    {
        int i1 =oprnd1.getRealSerialNumber();
        int i2 =oprnd2.getRealSerialNumber();
        int dstidx=dst.getRealSerialNumber();

        fileWriter.format("\tsub $t%d,$t%d,$t%d\n",dstidx, i1, i2);
        checkOverFlow(dst);
    }
    public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();
		int dstidx=dst.getRealSerialNumber();

		fileWriter.format("\tmul $t%d,$t%d,$t%d\n",dstidx,i1,i2);
        checkOverFlow(dst);
    }
    public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
    {
        int i1 =oprnd1.getRealSerialNumber();
        int i2 =oprnd2.getRealSerialNumber();
        int dstidx=dst.getRealSerialNumber();

        fileWriter.format("\tbeq $t%d,$zero,%s\n", i2, labels.get("division_by_zero"));
        fileWriter.format("\tdiv $t%d,$t%d,$t%d\n",dstidx,i1,i2);
        checkOverFlow(dst);
    }
    public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}
	public void blt(TEMP oprnd1, TEMP oprnd2, String label)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();

		fileWriter.format("\tblt $t%d,$t%d,%s\n",i1,i2,label);
        fileWriter.flush();
    }
    public void blt(TEMP oprnd1, String label, int sIdx)
    {
        int i1 =oprnd1.getRealSerialNumber();

        fileWriter.format("\tblt $t%d,$s%d,%s\n",i1,sIdx,label);
        fileWriter.flush();
    }
    public void bgt(TEMP oprnd1, String label, int sIdx)
    {
        int i1 =oprnd1.getRealSerialNumber();

        fileWriter.format("\tbgt $t%d,$s%d,%s\n",i1, sIdx, label);
        fileWriter.flush();
    }
    public void bgt(TEMP oprnd1, TEMP oprnd2, String label)
    {
        int i1 =oprnd1.getRealSerialNumber();
        int i2 =oprnd2.getRealSerialNumber();

        fileWriter.format("\tbgt $t%d,$t%d,%s\n",i1,i2,label);
        fileWriter.flush();
    }
    public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();

		fileWriter.format("\tbge $t%d,$t%d,%s\n",i1,i2,label);
	}
    public void ble(TEMP oprnd1,TEMP oprnd2,String label)
    {
        int i1 =oprnd1.getRealSerialNumber();
        int i2 =oprnd2.getRealSerialNumber();

        fileWriter.format("\tble $t%d,$t%d,%s\n",i1,i2,label);
        fileWriter.flush();
    }
    public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();

		fileWriter.format("\tbne $t%d,$t%d,%s\n",i1,i2,label);
        fileWriter.flush();
    }
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getRealSerialNumber();
		int i2 =oprnd2.getRealSerialNumber();

		fileWriter.format("\tbeq $t%d,$t%d,%s\n",i1,i2,label);
        fileWriter.flush();
    }
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getRealSerialNumber();

		fileWriter.format("\tbeq $t%d,$zero,%s\n",i1,label);
	}

    public void addStrings(TEMP dst, TEMP t1, TEMP t2)
    {
        int dstIdx = dst.getRealSerialNumber();
        pushFunctionParamToStack(t2);
        pushFunctionParamToStack(t1);

        fileWriter.format("\tjal %s\n", labels.get("str_concat"));
        fileWriter.format("\tmove $t%d, $v0\n", dstIdx);
        fileWriter.flush();
    }

    public void eqStrings(TEMP dst, TEMP t1, TEMP t2)
    {
        int dstIdx = dst.getRealSerialNumber();
        fileWriter.format("\tmove $a0, $t%d\n", t1.getRealSerialNumber());
        fileWriter.format("\tmove $a1, $t%d\n", t2.getRealSerialNumber());
        fileWriter.format("\tjal %s\n", labels.get("strcmp"));
        fileWriter.format("\tmove $t%d, $v0\n", dstIdx);
    }

    public void createVtable(List<List<String>> vtable, String className) {
        //fileWriter.format(".data\n");
       // fileWriter.format("vt_112233_" + className + ":\n");
        for (List<String> func : vtable) {
            String funcName = "func_" + func.get(0) + "_" + func.get(1) + "_" + func.get(2);
            fileWriter.format("\t.word %s\n", funcName);
        }
        fileWriter.flush();
    }

    public void loadThisInstance(TEMP thisInstance, int offset) {
        int thisInstance_idx = thisInstance.getRealSerialNumber();
        fileWriter.format("\tlw $t%d, 8($fp)\n", thisInstance_idx);
        fileWriter.format("\taddi $t%d, $t%d, %d\n", thisInstance_idx, thisInstance_idx, offset);
        fileWriter.flush();
    }

    public void loadGlobalVar(TEMP dst, String name)
    {
        fileWriter.format("\tla $t%d, g_%s\n", dst.getRealSerialNumber(), name);
        fileWriter.flush();
    }

    public void storeGlobal(String globalName, TEMP value)
    {
        int valueIdx = value.getRealSerialNumber();
        fileWriter.format("\tsw $t%d, g_%s\n", valueIdx, globalName);
        fileWriter.flush();
    }
    public void saveSpToS0()
    {
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $s0, 0($sp)\n");
        fileWriter.format("\tmove $s0, $sp\n");
        fileWriter.flush();
    }
    public void restoreSpFromS0()
    {
        fileWriter.format("\tmove $sp, $s0\n");
        fileWriter.format("\tlw $s0, 0($sp)\n");
        fileWriter.format("\taddi $sp, $sp, 4\n");
        fileWriter.flush();
    }

    public void storeFieldForInstance(int offset, TEMP instance, TEMP value)
    {
        int instanceIdx = instance.getRealSerialNumber();
        int valueIdx = value.getRealSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, %s\n", instanceIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tsw $t%d, %d($t%d)\n", valueIdx, offset, instanceIdx);
        fileWriter.flush();
    }

    public void allocateNewInstance(int classSize, TEMP newInstance, String vtLabel) {
        int instanceIdx = newInstance.getRealSerialNumber();
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tli $a0, %d\n", classSize);
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove $t%d, $v0\n", instanceIdx);
        fileWriter.format("\tla $s0, %s\n", vtLabel);
        fileWriter.format("\tsw $s0, 0($t%d)\n", instanceIdx);
        fileWriter.flush();
    }

    public void pushFunctionParamToStack(TEMP paramTemp) {
        int paramIdx = paramTemp.getRealSerialNumber();
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t%d, 0($sp)\n", paramIdx);
        fileWriter.flush();
    }

    public void virtualCall(TEMP dst, TEMP varTemp, int funcOffset, TEMP_LIST paramTemps) {
        int tmpIdx;
        int varTempIdx = varTemp.getRealSerialNumber();
        for (TEMP_LIST tmp = paramTemps ; tmp != null ; tmp = tmp.tail) {
            tmpIdx = tmp.head.getRealSerialNumber();
            fileWriter.format("\tsubu $sp, $sp, 4\n");
            fileWriter.format("\tsw $t%d, 0($sp)\n", tmpIdx);
        }
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t%d, 0($sp)\n", varTempIdx);
        fileWriter.format("\tlw $s0, 0($t%d)\n", varTempIdx);
        fileWriter.format("\tlw $s1, %d($s0)\n", funcOffset);
        fileWriter.format("\tjalr $s1\n");
     //   fileWriter.format("\taddu $sp, $sp, 8\n");

        if (dst != null)
            fileWriter.format("\tmove $t%d, $v0\n", dst.getRealSerialNumber());

        fileWriter.flush();
    }

    public void funcGlobalCall(TEMP dst, String funcName, TEMP_LIST paramTemps) {
        int tmpIdx;
        for (TEMP_LIST tmp = paramTemps ; tmp != null ; tmp = tmp.tail) {
            tmpIdx = tmp.head.getRealSerialNumber();
            fileWriter.format("\tsubu $sp, $sp, 4\n");
            fileWriter.format("\tsw $t%d, 0($sp)\n", tmpIdx);
        }
        fileWriter.format("\tjal %s\n", funcName);
     //   fileWriter.format("\taddu $sp, $sp, 8\n");

        if (dst != null)
            fileWriter.format("\tmove $t%d, $v0\n", dst.getRealSerialNumber());

        fileWriter.flush();
    }

    public void fieldSet(TEMP dst, TEMP t1) {
        int dstIdx = dst.getRealSerialNumber();
        int t1Idx = t1.getRealSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, %s\n", dstIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tsw $t%d, 0($t%d)\n", t1Idx, dstIdx);
        fileWriter.flush();
    }

    public void stringExpression(TEMP dst, Integer str)
    {
        int dst_idx = dst.getRealSerialNumber();
        fileWriter.format("\tla $t%s, const_string_%d\n", dst_idx, str);
        fileWriter.flush();
    }

    public void storeLocal(Integer offset, TEMP dst){
        int dstIdx = dst.getRealSerialNumber();
        fileWriter.format("\tsw $t%d, %d($fp)\n", dstIdx, offset);
        fileWriter.flush();
    }

    public void loadVariable(TEMP dst, Integer offset){
        int dstIdx = dst.getRealSerialNumber();
        fileWriter.format("\taddi $t%d, $fp, %d\n", dstIdx, offset);
        fileWriter.flush();
    }

    public void fieldAccess(TEMP dst, TEMP var_temp, int fieldOffset){
        int dstIdx = dst.getRealSerialNumber();
        int var_tempIdx = var_temp.getRealSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, %s\n", var_tempIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tlw $t%d, 0($t%d)\n",dstIdx, var_tempIdx);
        fileWriter.format("\tbeq $t%d, $zero, %s\n", dstIdx, labels.get("invalid_pointer_dereference"));
        fileWriter.format("\taddi $t%d, $t%d, %d\n", dstIdx, dstIdx, fieldOffset);
        fileWriter.flush();
    }


    public void arrayAllocate(TEMP arr, TEMP size){
        fileWriter.format("\tmove $a0, $t%d\n", size.getRealSerialNumber());
        fileWriter.format("\tadd $a0, $a0, 1\n");
        fileWriter.format("\tmul $a0, $a0, 4\n");
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove $t%d, $v0\n", arr.getRealSerialNumber());
        fileWriter.format("\tsw $t%d, 0($t%d)\n", size.getRealSerialNumber(), arr.getRealSerialNumber());

        fileWriter.flush();
    }


    public void arrayAccess(TEMP dst, TEMP var, TEMP subscript){
        int varIdx = var.getRealSerialNumber();
        int offsetIdx = subscript.getRealSerialNumber();
        int dstIdx = dst.getRealSerialNumber();

        fileWriter.format("\tbeq $t%d, $zero, %s\n", varIdx, labels.get("invalid_pointer_dereference"));
        String out_of_bounds = IRcommand.getFreshLabel("out_of_bounds");

        fileWriter.format("\tbltz $t%d, %s\n", offsetIdx, out_of_bounds);
        fileWriter.format("\tlw $s1, 0($t%d)\n", varIdx); // s1 = array.length
        fileWriter.format("\tbge $t%d, $s1, %s\n", offsetIdx, out_of_bounds); // check out of bounds
        fileWriter.format("\tmove $s0, $t%d\n", offsetIdx);
        fileWriter.format("\tadd $s0, $s0, 1\n");
        fileWriter.format("\tmul $s0, $s0, 4\n");
        fileWriter.format("\taddu $s0, $t%d, $s0\n", varIdx);
        fileWriter.format("\tmove $t%d, $s0\n", dstIdx);
        String end = IRcommand.getFreshLabel("end");
        fileWriter.format("\tj %s\n", end);

        fileWriter.format("%s:\n", out_of_bounds);
        fileWriter.print("\tla $t0, string_access_violation\n");
        printErrorMessage();
        fileWriter.format("\tjal %s\n", labels.get("Exit")); // exit

        fileWriter.format("%s:\n", end);
        fileWriter.flush();

    }

    public void setArrayVal(TEMP arrayField, TEMP val) {
        fileWriter.format("\tbeq $t%d, $zero, %s\n", arrayField.getRealSerialNumber(), labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tsw $t%d, 0($t%d)\n", val.getRealSerialNumber(), arrayField.getRealSerialNumber());
        fileWriter.flush();
    }

    public void storeAssignSTMT(TEMP dst, TEMP src) {
        fileWriter.format("\tbeq $t%d, $zero, %s\n", dst.getRealSerialNumber(), labels.get("invalid_pointer_dereference"));
        fileWriter.format("\tsw $t%d, 0($t%d)\n", src.getRealSerialNumber(), dst.getRealSerialNumber());
        fileWriter.flush();
    }

    public void allocateNewClass(TEMP dst, String allocationClassName)
    {
        fileWriter.format("\tjal %s\n", allocationClassName);
        fileWriter.format("\tmove $t%d, $v0\n", dst.getRealSerialNumber());
        fileWriter.flush();
    }

    public void funcDecPrologue(int numOfLocalVars) {
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $ra 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $fp 0($sp)\n");
        fileWriter.format("\tmove $fp, $sp\n");

        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t0, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t1, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t2, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t3, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t4, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t5, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t6, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t7, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t8, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t9, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");

        fileWriter.format("\tsubu $sp, $sp, " + (numOfLocalVars * 4) + "\n");
        fileWriter.flush();
    }

    public void funcDecEpilogue() {
        fileWriter.format("\tmove $sp, $fp\n");
        fileWriter.format("\tlw $t0, -4($sp)\n");
        fileWriter.format("\tlw $t1, -8($sp)\n");
        fileWriter.format("\tlw $t2, -12($sp)\n");
        fileWriter.format("\tlw $t3, -16($sp)\n");
        fileWriter.format("\tlw $t4, -20($sp)\n");
        fileWriter.format("\tlw $t5, -24($sp)\n");
        fileWriter.format("\tlw $t6, -28($sp)\n");
        fileWriter.format("\tlw $t7, -32($sp)\n");
        fileWriter.format("\tlw $t8, -36($sp)\n");
        fileWriter.format("\tlw $t9, -40($sp)\n");
        fileWriter.format("\tlw $fp, 0($sp)\n");
        fileWriter.format("\tlw $ra, 4($sp)\n");
        fileWriter.format("\taddu $sp, $sp, 8\n");

        fileWriter.format("\tjr $ra\n");
        fileWriter.flush();
    }

    public void returnFunc(TEMP returnValue)
    {
        fileWriter.format("\tmove $v0,$t%d\n", returnValue.getRealSerialNumber());
        fileWriter.flush();
    }

    public void strcmpFunc() {

        String strcmp = IRcommand.getFreshLabel("strcmp");
        labels.put("strcmp", strcmp);
        fileWriter.format("%s:\n", strcmp);

        // function prologue
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t0 0($sp)\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t1 0($sp)\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t2 0($sp)\n");

        fileWriter.print("\tadd $t0,$zero,$zero\n");
        fileWriter.print("\tadd $t1,$zero,$a0\n");
        fileWriter.print("\tadd $t2,$zero,$a1\n");

        String strcmp_loop = IRcommand.getFreshLabel("strcmp_loop");
        fileWriter.format("%s:\n", strcmp_loop);
        fileWriter.print("\tlb $t3($t1)\n");
        fileWriter.print("\tlb $t4($t2)\n");

        String firstStrId = IRcommand.getFreshLabel("first_Str_Id");
        fileWriter.format("\tbeqz $t3,%s\n", firstStrId);

        String secondStrId = IRcommand.getFreshLabel("second_Str_Id");
        fileWriter.format("\tbeqz $t4,%s\n", secondStrId);
        fileWriter.print("\tslt $t5,$t3,$t4\n");
        fileWriter.format("\tbnez $t5,%s\n" , secondStrId);
        fileWriter.print("\taddi $t1,$t1,1\n");
        fileWriter.print("\taddi $t2,$t2,1\n");
        fileWriter.format("\tj %s\n", strcmp_loop);

        fileWriter.format("\t%s: \n", secondStrId);
        fileWriter.print("\tadd $v0,$zero,$zero\n");
        String endStrcmp = IRcommand.getFreshLabel("end_strcmp");
        fileWriter.format("\tj %s\n", endStrcmp);

        fileWriter.format("\t%s:\n", firstStrId);
        fileWriter.format("\tbnez $t4,%s\n", secondStrId);
        fileWriter.print("\taddi $v0,$zero,1\n");


        // function epilogue
        fileWriter.format("%s:\n", endStrcmp);
        fileWriter.print("\tlw $t0 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $t1 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $t2 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tjr $ra\n");

    }

    public String strLenFunc() {
        String strLen = IRcommand.getFreshLabel("strlen");
        fileWriter.format("%s:\n", strLen);
        // prologue
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t0 0($sp)\n");

        fileWriter.print("\tadd $v0, $zero, $zero\n");

        String loop = IRcommand.getFreshLabel("loop");
        String endLoop = IRcommand.getFreshLabel("end");

        fileWriter.format("%s:\n", loop);
        fileWriter.print("\tlbu $t0, 0($a0)\n");               // t0 = str[i]
        fileWriter.print("\taddi $a0, $a0, 1\n");              // str++
        fileWriter.print("\taddi $v0, $v0, 1\n");              // len++
        fileWriter.format("\tbne $t0, $zero, %s\n", loop);         // while (t0 != end)

        fileWriter.format("%s:\n", endLoop);
        fileWriter.print("\taddi $v0, $v0, -1\n");        // len--

        // epilogue
        fileWriter.print("\tlw $t0, 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tj $ra\n");

        return strLen;
    }

    public String strCopierFunc() {
        String strCopier = IRcommand.getFreshLabel("strcopier");
        fileWriter.format("%s:\n", strCopier);

        fileWriter.print("\tor $t0, $a0, $zero\n"); // t0 = a0
        fileWriter.print("\tor $t1, $a1, $zero\n"); // t1 = a1 (result)

        String loop = IRcommand.getFreshLabel("loop");
        String endLoop = IRcommand.getFreshLabel("end");

        fileWriter.format("%s:\n", loop);
        fileWriter.print("\tlb $t2, 0($t0)\n"); // str[i]
        fileWriter.format("\tbeq $t2, $zero, %s\n", endLoop); // while str[i] != end
        fileWriter.print("\taddiu $t0, $t0, 1\n"); // i++
        fileWriter.print("\tsb $t2, 0($t1)\n"); // t1 is the result
        fileWriter.print("\taddiu $t1, $t1, 1\n");
        fileWriter.format("\tj %s\n", loop);

        fileWriter.format("%s:\n", endLoop);
        fileWriter.print("\tor $v0, $t1, $zero\n"); // v0 = t1
        fileWriter.print("\tjr $ra\n");
        return strCopier;
    }

    public void strConcatFunc() {
        String strLen = strLenFunc();
        String strCopier = strCopierFunc();

        String str_concat = IRcommand.getFreshLabel("str_concat");
        labels.put("str_concat", str_concat);
        fileWriter.format("%s:\n", str_concat);

        // function prologue
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $ra 0($sp)\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $fp 0($sp)\n");
        fileWriter.print("\tmove $fp, $sp\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t0 0($sp)\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t1 0($sp)\n");
        fileWriter.print("\tsubu $sp, $sp, 4\n");
        fileWriter.print("\tsw $t2 0($sp)\n");

        // Copy first string to result buffer
        fileWriter.print("\tlw $t0, 8($fp)\n"); // str1
        fileWriter.print("\tmove $a0, $t0\n"); // a0 = str1
        fileWriter.format("\tjal %s\n", strLen);
        fileWriter.print("\tmove $t1, $v0\n"); // t1 = strlen(str1)

        fileWriter.print("\tlw $t0, 12($fp)\n"); // str2
        fileWriter.print("\tmove $a0, $t0\n"); // a0 = str2
        fileWriter.format("\tjal %s\n", strLen);
        fileWriter.print("\tmove $t0, $v0\n"); // t0 = strlen(str2)

        fileWriter.print("\tadd $t0, $t0, $t1\n"); // t0 = t0 + t1
        fileWriter.print("\taddi $t0, $t0, 1\n"); // t0++

        fileWriter.print("\tmove $a0, $t0\n"); // allocate on heap
        fileWriter.print("\tli $v0, 9\n");
        fileWriter.print("\tsyscall\n");

        fileWriter.print("\tmove $a1, $v0\n"); // new string address on $a1
        fileWriter.print("\tlw $t0, 8($fp)\n"); // str1
        fileWriter.print("\tmove $a0, $t0\n"); // a0 = str1
        fileWriter.format("\tjal %s\n", strCopier);

        // Concatenate second string on result buffer
        fileWriter.print("\tlw $t0, 12($fp)\n"); // str2
        fileWriter.print("\tmove $a0, $t0\n"); // a0 = str2
        fileWriter.print("\tor $a1, $v0, $zero\n"); // a1 = endOf(t1)
        fileWriter.format("\tjal %s\n", strCopier);

        // function epilogue
        fileWriter.print("\tlw $t2 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $t1 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $t0 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $fp 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tlw $ra 0($sp)\n");
        fileWriter.print("\taddi $sp, $sp, 4\n");
        fileWriter.print("\tjr $ra\n");
    }


    /**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");

            instance.fileWriter.print("space: .asciiz \" \"\n");
            instance.fileWriter.print("new_line: .asciiz \"\\n\"\n");

            List<String> strings = IR.getInstance().getStrings();
            for (int i = 0; i < strings.size(); i++) {
                String str = strings.get(i);
                instance.fileWriter.print(String.format("const_string_%d: .asciiz \"%s\"\n", i, str));
            }

            List<String> globals = IR.getInstance().getGlobals();
            for (String global : globals) {
                instance.fileWriter.print(String.format("g_%s: .word 0xdeadbeef\n", global));
            }
            instance.fileWriter.print(".text\n");

            instance.strConcatFunc();
            instance.strcmpFunc();

            instance.Exit();
            instance.Malloc();
            instance.invalid_pointer_dereference();
            instance.division_by_zero();
		}
		return instance;
	}
}
