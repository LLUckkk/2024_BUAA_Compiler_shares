package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.*;

public class GetIntInstr extends Instr {
    public GetIntInstr(String name) {
        super(BaseType.INT32, name, InstrType.IO);
    }

    public static String getDeclareCode() {
        return "declare i32 @getint();";
    }

    public String toString() {
        return name +
                " = call i32 @getint()\n";
    }

    public void toMips() {
        super.toMips();
        new LiAsmb(Register.V0, 5);
        new SyscallAsmb();
        //主要在于对这个值怎么处理
        if (MipsManager.getInstance().findReg(this) != null) {
            Register reg = MipsManager.getInstance().findReg(this);
            new MoveAsmb(reg, Register.V0);
        } else {
            MipsManager.getInstance().storeValue(this, 4);
            int curOffset = MipsManager.getInstance().getCurOffset();
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, Register.V0);
        }
    }
}
