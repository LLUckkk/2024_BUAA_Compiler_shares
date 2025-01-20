package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.StringLiteral;
import llvm_ir.type.BaseType;
import llvm_ir.type.PointerType;

public class PutStrInstr extends Instr {
    private StringLiteral str;

    public PutStrInstr(String name, StringLiteral str) {
        super(BaseType.VOID, name, InstrType.IO);
        this.str = str;
    }

    public static String getDeclareCode() {
        return "declare void @putstr(i8*)";
    }

    public String toString() {
        PointerType ptr_type = (PointerType) str.getType();
        return "call void @putstr(i8* getelementptr inbounds (" +
                ptr_type.getDestType().toString() +
                ", " +
                ptr_type + " " +
                str.getName() +
                ", i64 0, i64 0))";

    }
}
