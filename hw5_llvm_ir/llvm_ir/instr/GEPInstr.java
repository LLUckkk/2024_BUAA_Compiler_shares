package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

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
}
