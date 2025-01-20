package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.BaseType;

public class GetCharInstr extends Instr {
    public GetCharInstr(String name) {
        super(BaseType.INT32, name, InstrType.IO);
    }

    public static String getDeclareCode(){
        return "declare i32 @getchar()";
    }

    public String toString() {
        return name +
                " = call i32 @getchar()\n";
    }
}
