package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

public class AllocaInstr extends Instr {
    public LLVMType destType;

    public AllocaInstr(LLVMType destType, String name) {
        super(new PointerType(destType), name, InstrType.ALLOCA);
        this.destType = destType;
    }

    //<result> = alloca <type>
    public String toString() {
        return name +
                " = alloca " +
                destType.toString();
    }


}
