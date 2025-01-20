package llvm_ir.instr;

import llvm_ir.GlobalVar;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LaAsmb;
import mips.assembly.MemoryAsmb;

public class LoadInstr extends Instr {
    private Value pointer;

    public LoadInstr(String name, Value pointer) {
        super(((PointerType) pointer.getType()).getDestType(), name, InstrType.LOAD);
        addOperand(pointer);
        this.pointer = pointer;
    }

    //result> = load <ty>, ptr <pointer>
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = load ");
        //注意这个地方需要解引用之后的类型
        sb.append(type);
        sb.append(", ");
        sb.append(pointer.getType());
        sb.append(" ");
        sb.append(pointer.getName());
        return sb.toString();
    }

    public LLVMType getDestType() {
        return ((PointerType) pointer.getType()).getDestType();
    }

    public void toMips() {
        super.toMips();
        //先找到pointer的具体值
        Register ptrReg = Register.K0;
        Register destReg;
        if ((destReg = MipsManager.getInstance().findReg(this)) == null) {
            destReg = Register.K1;//为啥写得是k0
        }
        //这个地方要找到ptr的具体值
        if (pointer instanceof GlobalVar) {
            new LaAsmb(ptrReg, pointer.getName().substring(1));
        } else if (MipsManager.getInstance().findReg(pointer) != null) {
            ptrReg = MipsManager.getInstance().findReg(pointer);
        } else {
            int offset = MipsManager.getInstance().loadValue(pointer);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, ptrReg);
        }
        //load
        if (getDestType().isChar8()) {
            new MemoryAsmb(MemoryAsmb.MemoryOp.LB, ptrReg, 0, destReg);
        } else {
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, ptrReg, 0, destReg);
        }
        //对当前值存储
        if (MipsManager.getInstance().findReg(this) == null) {
            if (getDestType().isChar8()) {
                int curOffset = MipsManager.getInstance().storeValue(this, 1);
                new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP, curOffset, destReg);
            } else {
                int curOffset = MipsManager.getInstance().storeValue(this, 4);
                new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, destReg);
            }
        }
    }
}
