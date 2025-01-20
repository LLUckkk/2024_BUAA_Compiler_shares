package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.BaseType;

public class GetIntInstr extends Instr {
    public GetIntInstr(String name) {
        super(BaseType.INT32, name, InstrType.IO);
    }

    public static String getDeclareCode() {
        return "declare i32 @getint();";
    }

    public String toString() {
        return name +
                " = call i32 @getint()\n";
    }
}
