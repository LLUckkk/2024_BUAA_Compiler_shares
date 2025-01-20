package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

public class LoadInstr extends Instr {
    private Value pointer;

    public LoadInstr(String name, Value pointer) {
        super(((PointerType) pointer.getType()).getDestType(), name, InstrType.LOAD);
        addOperand(pointer);
        this.pointer = pointer;
    }

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
}
