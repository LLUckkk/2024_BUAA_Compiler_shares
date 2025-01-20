package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class PutIntInstr extends Instr {
    public PutIntInstr(String name, Value dest) {
        super(BaseType.VOID, name, InstrType.IO);
        addOperand(dest);
    }

    public Value getDest() {
        return operands.get(0);
    }

    public static String getDeclareCode() {
        return "declare void @putint(i32);";
    }

    public String toString() {
        return "call void @putint(i32 " + getDest().getName() + ")\n";
    }
}
