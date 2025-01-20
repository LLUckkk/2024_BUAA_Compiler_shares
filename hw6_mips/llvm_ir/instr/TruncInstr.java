package llvm_ir.instr;

import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;

public class TruncInstr extends Instr {
    private LLVMType src;
    private LLVMType dest;
    private Value srcValue;

    public TruncInstr(String name, LLVMType src, LLVMType dest, Value srcValue) {
        super(dest, name, InstrType.TRUNC);
        addOperand(srcValue);
        this.src = src;
        this.dest = dest;
        this.srcValue = srcValue;
    }

    //<result> = trunc <ty> <value> to <ty2>
    public String toString() {
        return name +
                " = trunc " +
                src.toString() +
                " " +
                srcValue.getName() +
                " to " +
                dest.toString();
    }

    public void toMips() {
        super.toMips();
        //i1 i32 或者i8 i32
        Register srcReg;
        if ((srcReg = MipsManager.getInstance().findReg(srcValue)) != null) {
            MipsManager.getInstance().storeValue(this, 4);
            int curOffset = MipsManager.getInstance().getCurOffset();
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, srcReg);
        } else {
            Integer srcOffset = MipsManager.getInstance().findOffset(srcValue);
            if (srcOffset == null) {
                if (srcValue instanceof Constant) {
                    new LiAsmb(Register.K0, ((Constant) srcValue).getValue());
                    int offset = MipsManager.getInstance().storeValue(this, 1);
                    new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, offset, Register.K0);
                }
            } else {
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, srcOffset, Register.K0);
                srcOffset = MipsManager.getInstance().storeValue(this, 1);
                new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, srcOffset, Register.K0);
            }
        }
    }
}
