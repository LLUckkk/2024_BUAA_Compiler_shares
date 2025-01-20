package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

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

    public String toString() {
        return "call void @putch(i32 " + getDest().getName() + ")\n";
    }
}
