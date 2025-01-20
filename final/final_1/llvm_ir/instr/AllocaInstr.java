package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.ArrayType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.AluAsmb;
import mips.assembly.MemoryAsmb;

public class AllocaInstr extends Instr {
    public LLVMType destType;

    public AllocaInstr(LLVMType destType, String name) {
        super(new PointerType(destType), name, InstrType.ALLOCA);
        this.destType = destType;
    }

    //<result> = alloca <type>
    public String toString() {
        return name +
                " = alloca " +
                destType.toString();
    }

    public boolean willUse() {
        return true;
    }

    // %1=alloca i32
    public void toMips() {
        super.toMips();
        //alloca的结果是一个指针
        //无论是否有寄存器对应，都要在栈上分配空间，因为寄存器存的是地址，不是真正的值
        if (destType.isArray()) {
            int size = ((ArrayType) destType).getElementNum();
            if (((ArrayType) destType).isCharArray()) {
                MipsManager.getInstance().subOffset(size);
            } else {
                int cur = MipsManager.getInstance().getCurOffset();
                int delta = size * 4;
                if ((-cur) % 4 != 0) {
                    delta += 4 - (-cur) % 4;
                }
                MipsManager.getInstance().subOffset(delta);
            }
        } else if (destType.isChar8()) {
            MipsManager.getInstance().subOffset(1);
        } else if (destType.isInt32()) {
            int cur = MipsManager.getInstance().getCurOffset();
            int delta = 4;
            if ((-cur) % 4 != 0) {
                delta += 4 - (-cur) % 4;
            }
            MipsManager.getInstance().subOffset(delta);
        } //这个申请的空间的地址存给了可能的寄存器，没有的话就存到栈上
        //如果这个alloca的结果赋给的变量有对应的寄存器
        Register reg;
        if ((reg = MipsManager.getInstance().findReg(this)) != null) {
            //把当前的位置写入寄存器，也就是以后要去这个值直接上这个寄存器里找地址
            new AluAsmb(AluAsmb.AluOp.ADDI, reg, Register.SP, MipsManager.getInstance().getCurOffset());
        } else {
            //把指针值存入寄存器中：
            //为啥要保存分配空间的首地址，k0不是一直会刷新吗,这里只是把k0当成临时寄存器，后续还会存到栈上
            int cur = MipsManager.getInstance().getCurOffset();
            new AluAsmb(AluAsmb.AluOp.ADDI, Register.K0, Register.SP, cur);
            //把指针存入栈中：为什么不直接存，因为SW不能直接常数存
            cur = MipsManager.getInstance().storeValue(this, 4);
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, cur, Register.K0);
        }
    }
}
