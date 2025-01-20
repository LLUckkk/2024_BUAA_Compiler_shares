package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;
import mips.assembly.MoveAsmb;

public class MoveInstr extends Instr {


    public MoveInstr(String name, Value dest, Value src) {
        super(BaseType.VOID, name, InstrType.MOVE);
        addOperand(dest);
        addOperand(src);
    }

    public Value getDest() {
        return operands.get(0);
    }

    public Value getSrc() {
        return operands.get(1);
    }

    public void setDest(Value dest) {
        operands.set(0, dest);
    }

    public void setSrc(Value src) {
        operands.set(1, src);
    }

    public String toString() {
        Value dest = getDest();
        Value src = getSrc();
        return "move " + dest.getType() + " " + dest.getName() + " " + src.getName();
    }

    public void toMips() {
        super.toMips();

        Value dest = getDest();
        Value src = getSrc();

        Register destReg = MipsManager.getInstance().findReg(dest);
        Register srcReg = MipsManager.getInstance().findReg(src);
        if (destReg != null && destReg.equals(srcReg)) {
            return;
        }
        if (destReg == null) {
            destReg = Register.K0;
        }

        if (src instanceof Constant) {
            new LiAsmb(destReg, ((Constant) src).getValue());
        } else if (src instanceof Undefined) {
            new LiAsmb(destReg, 0);
        } else if (srcReg != null) {
            new MoveAsmb(destReg, srcReg);
        } else {
            int srcOffset = MipsManager.getInstance().loadValue(src);
            if (src.getType().isChar8()) {
                new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, srcOffset, destReg);
            } else {
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, srcOffset, destReg);
            }
        }

        if (MipsManager.getInstance().findReg(dest) == null) {
            Integer destOffset = MipsManager.getInstance().findOffset(dest);
            if (destOffset == null) {
                if (dest.getType().isInt32()) {
                    destOffset = MipsManager.getInstance().storeValue(dest, 4);
                } else {
                    destOffset = MipsManager.getInstance().storeValue(dest, 1);
                }
            }
            if (dest.getType().isChar8()) {
                new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, destOffset, destReg);
            } else {
                new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, destOffset, destReg);
            }
        }
    }
}
