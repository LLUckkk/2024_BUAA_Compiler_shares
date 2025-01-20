package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LaAsmb;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;

public class StoreInstr extends Instr {

    public StoreInstr(String name, Value from, Value to) {
        super(BaseType.VOID, name, InstrType.STORE);
        addOperand(from);
        addOperand(to);
    }

    public Value getFrom() {
        return operands.get(0);
    }

    public Value getTo() {
        return operands.get(1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("store ");
        sb.append(getFrom().getType());
        sb.append(" ");
        sb.append(getFrom().getName());
        sb.append(", ");
        sb.append(getTo().getType());
        sb.append(" ");
        sb.append(getTo().getName());
        return sb.toString();
    }

    public void toMips() {
        super.toMips();
        Value from = getFrom();//from是一个值
        Value to = getTo(); //to是一个指针
        Register fromReg = Register.K0;
        Register toReg = Register.K1;
        //获取to的具体值
        if (to instanceof GlobalVar) {
            new LaAsmb(toReg, to.getName().substring(1));
        } else if (MipsManager.getInstance().findReg(to) != null) {
            toReg = MipsManager.getInstance().findReg(to);
        } else {
            int offset = MipsManager.getInstance().loadValue(to);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, toReg);
        }
        //获取from的具体值
        if (from instanceof GlobalVar) {
            new LaAsmb(fromReg, from.getName().substring(1)); //但是现在要拿的是from的具体的值
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, fromReg, 0, fromReg);
        } else if (MipsManager.getInstance().findReg(from) != null) {
            fromReg = MipsManager.getInstance().findReg(from);
        } else if (from instanceof Constant) {
            new LiAsmb(fromReg, ((Constant) from).getValue());
        } else {
            int offset = MipsManager.getInstance().loadValue(from);
            if (from.getType().isChar8()) {
                new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, offset, fromReg);
            } else {
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, fromReg);
            }
        }
        if (((PointerType) to.getType()).getDestType().isChar8()) {
            new MemoryAsmb(MemoryAsmb.MemoryOp.SB, toReg, 0, fromReg);
        } else {
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, toReg, 0, fromReg);
        }
    }
}
