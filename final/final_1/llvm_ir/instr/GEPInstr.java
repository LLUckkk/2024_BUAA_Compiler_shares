package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.AluAsmb;
import mips.assembly.LaAsmb;
import mips.assembly.MemoryAsmb;

//gep指令，计算地址

public class GEPInstr extends Instr {
    private LLVMType elemType;

    public GEPInstr(String name, Value start, Value offset, LLVMType elemType) {
        super(new PointerType(elemType), name, InstrType.GEP);
        addOperand(start);
        addOperand(offset);
        this.elemType = elemType;
    }

    public Value getStart() {
        return operands.get(0);
    }

    public Value getOffset() {
        return operands.get(1);
    }

    public boolean willUse(){
        return true;
    }

    public String toString() {
        PointerType startType = (PointerType) getStart().getType();
        if ((startType.getDestType().isArray())) {
            return name +
                    " = getelementptr inbounds " +
                    ((PointerType) getStart().getType()).getDestType().toString() +
                    ", " +
                    getStart().getType().toString() +
                    " " +
                    getStart().getName() +
                    ", " +
                    "i32" +
                    " 0, " +
                    "i32 " +
                    getOffset().getName();
        } else {
            return name +
                    " = getelementptr inbounds " +
                    ((PointerType) getStart().getType()).getDestType().toString() +
                    ", " +
                    getStart().getType().toString() +
                    " " +
                    getStart().getName() +
                    ", " +
                    "i32 " +
                    getOffset().getName();
        }
    }

    public void toMips() {
        super.toMips();
        //gep获取指令
        Value start = getStart();
        Value offset = getOffset();
        Register startReg = Register.K0;
        Register offsetReg = Register.K1;
        Register destReg = MipsManager.getInstance().findReg(this);
        if (destReg == null) {
            destReg = Register.K0;
        }

        //首先拿到start的值：
        if (start instanceof GlobalVar) {
            new LaAsmb(startReg, start.getName().substring(1));
        } else if (MipsManager.getInstance().findReg(start) != null) {
            startReg = MipsManager.getInstance().findReg(start);
        } else {
            int stackOffset = MipsManager.getInstance().findOffset(start);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, stackOffset, startReg);
        }

        //再拿offset的值：
        if (offset instanceof Constant) {
            if (elemType.isChar8()) {
                new AluAsmb(AluAsmb.AluOp.ADDI, destReg, startReg, ((Constant) offset).getValue());
            } else {
                new AluAsmb(AluAsmb.AluOp.ADDI, destReg, startReg, ((Constant) offset).getValue() * 4);
            }
        } else {
            if (offset instanceof GlobalVar) {
                new LaAsmb(offsetReg, offset.getName().substring(1));
                if(offset.getType().isInt32()){
                    new MemoryAsmb(MemoryAsmb.MemoryOp.LW, offsetReg, 0, offsetReg);
                } else if(offset.getType().isChar8()){
                    new MemoryAsmb(MemoryAsmb.MemoryOp.LB, offsetReg, 0, offsetReg);
                }
            } else if (MipsManager.getInstance().findReg(offset) != null) {
                offsetReg = MipsManager.getInstance().findReg(offset);
            } else {
                int stackOffset = MipsManager.getInstance().loadValue(offset);
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, stackOffset, offsetReg);
            }
            if (elemType.isInt32()) {
                new AluAsmb(AluAsmb.AluOp.SLL, Register.K1, offsetReg, 2); //注意只有int类型的数组才sll，char类型的不需要
                new AluAsmb(AluAsmb.AluOp.ADDU, destReg, startReg, Register.K1);
            } else {
                new AluAsmb(AluAsmb.AluOp.ADDU, destReg, startReg, offsetReg);
            }
        }
        if (MipsManager.getInstance().findReg(this) == null) {
            int curOffset = MipsManager.getInstance().storeValue(this, 4);
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, destReg);
        }
    }
}
