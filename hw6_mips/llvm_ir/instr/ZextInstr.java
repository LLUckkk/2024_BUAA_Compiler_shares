package llvm_ir.instr;

import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;

public class ZextInstr extends Instr {
    private LLVMType destType;
    private Value src;

    public ZextInstr(String name, Value src, LLVMType destType) {
        super(destType, name, InstrType.ZEXT);
        this.destType = destType;
        this.src = src;
        addOperand(src);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = zext ");
        sb.append(src.getType());
        sb.append(" ");
        sb.append(src.getName());
        sb.append(" to ");
        sb.append(destType);
        return sb.toString();
    }

    public void toMips() {
        super.toMips();
        //i1 i32 或者i8 i32
        Register srcReg;
        if ((srcReg = MipsManager.getInstance().findReg(src)) != null) {
            MipsManager.getInstance().storeValue(this, 4);
            int curOffset = MipsManager.getInstance().getCurOffset();
            new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, curOffset, srcReg);
        } else {
            Integer srcOffset;
            if (MipsManager.getInstance().findOffset(src) == null) {
                if (src instanceof Constant) {
                    new LiAsmb(Register.K0, ((Constant) src).getValue());
                    int offset = MipsManager.getInstance().storeValue(this, 4);
                    new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, offset, Register.K0);
                }
            } else {
                srcOffset = MipsManager.getInstance().findOffset(src);
                new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, srcOffset, Register.K0);
                srcOffset = MipsManager.getInstance().storeValue(this, 4);
                new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, srcOffset, Register.K0);
            }
//            new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, )
//            MipsManager.getInstance().giveStack2Value(this, srcOffset);
        }
    }
}
