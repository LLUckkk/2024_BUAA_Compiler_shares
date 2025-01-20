package llvm_ir.instr;

import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.AluAsmb;
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

    public boolean willUse() {
        return true;
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

    //
    public void toMips() {
        super.toMips();
        Register reg = Register.K0;
        if (srcValue instanceof Constant) {
            new LiAsmb(reg, ((Constant) srcValue).getValue());
        } else if (MipsManager.getInstance().findReg(srcValue) != null) {
            reg = MipsManager.getInstance().findReg(srcValue);
        } else {
            int srcOffset = MipsManager.getInstance().findOffset(srcValue);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, srcOffset, reg);
        }
        if(MipsManager.getInstance().findReg(this) != null){
            Register thisReg = MipsManager.getInstance().findReg(this);
            new AluAsmb(AluAsmb.AluOp.AND, thisReg, reg, 0xFFFF);
        } else {
            int thisOffset = MipsManager.getInstance().loadValue(this);
            new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, thisOffset, reg);
        }
    }
}
