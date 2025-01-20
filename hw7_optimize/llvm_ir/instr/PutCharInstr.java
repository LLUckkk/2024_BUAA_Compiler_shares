package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;
import mips.assembly.MoveAsmb;
import mips.assembly.SyscallAsmb;

public class PutCharInstr extends Instr {
    public PutCharInstr(String name, Value dest) {
        super(BaseType.VOID, name, InstrType.IO);
        addOperand(dest);
    }

    public static String getDeclareCode() {
        return "declare void @putch(i32)";
    }

    public Value getDest() {
        return operands.get(0);
    }

    public boolean willUse() {
        return true;
    }

    public String toString() {
        return "call void @putch(i32 " + getDest().getName() + ")\n";
    }

    public void toMips() {
        super.toMips();
        Value dest = getDest();
        if (dest instanceof Constant) {
            new LiAsmb(Register.A0, ((Constant) dest).getValue());
        } else if (dest instanceof Undefined) {
            new LiAsmb(Register.A0, 0);
        } else if (MipsManager.getInstance().findReg(dest) != null) {
            Register destReg = MipsManager.getInstance().findReg(dest);
            new MoveAsmb(Register.A0, destReg);
        } else {
            int offset = MipsManager.getInstance().loadValue(dest);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, Register.A0);
        }
        new LiAsmb(Register.V0, 11);
        new SyscallAsmb();
    }
}
