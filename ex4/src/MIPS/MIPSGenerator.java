/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.Register_Allocation;
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//
	//	return t;
	//}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name);
	}
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
	}
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);
	}
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
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
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();

		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);
	}

    public void addStrings(TEMP dst, TEMP t1, TEMP t2)
    {
        int dstIdx = dst.getSerialNumber();

        pushFunctionParamToStack(t2);
        pushFunctionParamToStack(t1);
        fileWriter.format("\tjal concat\n");
        fileWriter.format("\tmove $t%s, $v0\n", dstIdx);
        fileWriter.flush();
    }

    public void eqStrings(TEMP dst, TEMP t1, TEMP t2)
    {
        int dstIdx = dst.getSerialNumber();
        fileWriter.format("\tmove $a0, $t%s\n", t1.getSerialNumber());
        fileWriter.format("\tmove $a1, $t%s\n", t2.getSerialNumber());
        fileWriter.format("\tjal strcmp\n");
        fileWriter.format("\tmove $t%s, $v0\n", dstIdx);
    }

    public void createVtable(List<List<String>> vtable, String className) {
        fileWriter.format("\t.data\n");
        fileWriter.format("\tvt_" + className + "\n");
        for (List<String> func : vtable) {
            fileWriter.format("\tfunc_" + func.get(0) + "_" + func.get(1) + "_" + func.get(2) + "\n");
        }
        fileWriter.flush();
    }

    public void loadThisInstance(TEMP thisInstance, int offset) {
        int thisInstance_idx = thisInstance.getSerialNumber();
        fileWriter.format("\tlw $t%s, 8($fp)\n", thisInstance_idx);
        fileWriter.format("\taddi $t%s, $t%s, %d\n", thisInstance_idx, thisInstance_idx, offset);
        fileWriter.flush();
    }

    public void loadGlobalVar(TEMP dst, String name)
    {
        fileWriter.format("\tla $t%s, g_%s\n", dst.getSerialNumber(), name);
        fileWriter.flush();
    }

    public void storeGlobal(String globalName, TEMP value)
    {
        int valueIdx = value.getSerialNumber();
        fileWriter.format("\tsw $t%s, g_%s\n", valueIdx, globalName);
        fileWriter.flush();
    }

    public void storeFieldForInstance(int offset, TEMP instance, TEMP value)
    {
        int instanceIdx = instance.getSerialNumber();
        int valueIdx = value.getSerialNumber();
        fileWriter.format("\tbeq $t%d, $zero, invalid_pointer_dereference\n", instanceIdx);
        fileWriter.format("\tsw $t%s, %d($t%s)\n", valueIdx, offset, instanceIdx);
        fileWriter.flush();
    }

    public void allocateNewInstance(int classSize, TEMP newInstance, String vtLabel) {
        int instanceIdx = newInstance.getSerialNumber();
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tli $a0, %d\n", classSize);
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove $t%d, $v0\n", instanceIdx);
        fileWriter.format("\tla $s0, %s\n", vtLabel);
        fileWriter.format("\tsw $s0, 0($t%d)\n", instanceIdx);
        fileWriter.flush();
    }

    public void pushFunctionParamToStack(TEMP paramTemp) {
        int paramIdx = paramTemp.getSerialNumber();
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t%d, 0($sp)\n", paramIdx);
        fileWriter.flush();
    }

    public void virtualCall(TEMP dst, TEMP varTemp, int funcOffset, TEMP_LIST paramTemps) {
        int tmpIdx;
        int varTempIdx = varTemp.getSerialNumber();
        for (TEMP_LIST tmp = paramTemps ; tmp != null ; tmp = tmp.tail) {
            tmpIdx = tmp.head.getSerialNumber();
            fileWriter.format("\tsubu $sp, $sp, 4\n");
            fileWriter.format("\tsw $t%d, 0($sp)\n", tmpIdx);
        }
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t%d, 0($sp)\n", varTempIdx);
        fileWriter.format("\tlw $s0, 0($t%d)\n", varTempIdx);
        fileWriter.format("\tlw $s1, %d($s0)\n", funcOffset);
        fileWriter.format("\tjalr $s1\n");
        fileWriter.format("\taddu $sp, $sp, 8\n");

        if (dst != null)
            fileWriter.format("\tmove $t%d, $v0\n", dst.getSerialNumber());

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
        fileWriter.format("\tsubu $sp, $sp, " + numOfLocalVars + "\n");
        fileWriter.flush();
    }


    public void funcDecEpilogue() {

        fileWriter.format("\tmove $sp, $fp\n");
        fileWriter.format("\tlw $fp, 0($sp)\n");
        fileWriter.format("\tlw $ra, 4($sp)\n");

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

        fileWriter.format("\taddu $sp, $sp, 8\n");

        fileWriter.format("\tjr $ra\n");
        fileWriter.flush();
    }

    public void returnFunc(TEMP returnValue)
    {
        fileWriter.format("\tmove $v0,$t%d\n", Register_Allocation.getIrRegisterToPhysical().get(returnValue.getSerialNumber()));
        fileWriter.flush();
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
		}
		return instance;
	}
}
