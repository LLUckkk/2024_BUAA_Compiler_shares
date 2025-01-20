package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.LLVMType;

public class ZextInstr extends Instr {
    private LLVMType destType;
    private Value src;
    public ZextInstr(String name, Value src, LLVMType destType){
        super(destType, name, InstrType.ZEXT);
        this.destType = destType;
        this.src = src;
        addOperand(src);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = zext ");
        sb.append(src.getType());
        sb.append(" ");
        sb.append(src.getName());
        sb.append(" to ");
        sb.append(destType);
        return sb.toString();
    }
}
