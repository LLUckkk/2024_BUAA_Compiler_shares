package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;

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

}
