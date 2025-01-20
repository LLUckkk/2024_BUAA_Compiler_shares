package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class ReturnInstr extends Instr {
    private Value retValue;

    public ReturnInstr(String name, Value retValue) {
        super(BaseType.VOID, name, InstrType.RETURN);
        if (retValue != null) {
            addOperand(retValue);
        }
        this.retValue = retValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ret ");
        if (retValue != null) {
            sb.append(retValue.getType());
            sb.append(" ");
            sb.append(retValue.getName());
        } else {
            sb.append("void");
        }
        return sb.toString();
    }
}
