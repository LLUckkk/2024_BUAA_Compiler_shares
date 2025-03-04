package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.JumpAsmb;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;
import mips.assembly.MoveAsmb;

public class ReturnInstr extends Instr {
    public ReturnInstr(String name, Value retValue) {
        super(BaseType.VOID, name, InstrType.RETURN);
//        if (retValue != null) {
//            addOperand(retValue);
//        }
        addOperand(retValue);
    }

    public Value getRetValue() {
        return operands.get(0);
    }

    public String toString() {
        Value retValue = getRetValue();
        StringBuilder sb = new StringBuilder();
        sb.append("ret ");
        if (retValue != null) {
            sb.append(retValue.getType());
            sb.append(" ");
            sb.append(retValue.getName());
        } else {
            sb.append("void");
        }
        return sb.toString();
    }

    public void toMips() {
        Value retValue = getRetValue();
        super.toMips();
        //如果不是void，把返回值放入v0
        //最后jr跳
        if (retValue != null) {
            if (retValue instanceof Constant) {
                new LiAsmb(Register.V0, ((Constant) retValue).getValue());
            } else if (retValue instanceof Undefined) {
                new LiAsmb(Register.V0, 0);
            } else if (MipsManager.getInstance().findReg(retValue) != null) {
                Register fromReg = MipsManager.getInstance().findReg(retValue);
                new MoveAsmb(Register.V0, fromReg);
            } else {
                int offset = MipsManager.getInstance().loadValue(retValue);
                if (retValue.getType().isChar8()) {
                    new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, offset, Register.V0);
                } else {
                    new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, Register.V0);
                }
            }
        }
        new JumpAsmb(JumpAsmb.JumpOp.JR, Register.RA);
    }
}
