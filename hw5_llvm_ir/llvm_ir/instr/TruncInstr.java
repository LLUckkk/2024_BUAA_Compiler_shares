package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;

public class TruncInstr extends Instr {
    private LLVMType src;
    private LLVMType dest;
    private Value srcValue;

    public TruncInstr(String name, LLVMType src, LLVMType dest, Value srcValue) {
        super(dest, name, InstrType.TRUNC);
        addOperand(srcValue);
        this.src = src;
        this.dest = dest;
        this.srcValue = srcValue;
    }

    //<result> = trunc <ty> <value> to <ty2>
    public String toString() {
        return name +
                " = trunc " +
                src.toString() +
                " " +
                srcValue.getName() +
                " to " +
                dest.toString();
    }
}
