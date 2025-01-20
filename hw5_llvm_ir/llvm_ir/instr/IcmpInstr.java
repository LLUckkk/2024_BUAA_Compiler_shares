package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class IcmpInstr extends Instr {
    public enum IcmpCond {
        EQ,
        NE,
        SGT,
        SGE,
        SLT,
        SLE
    }

    private IcmpCond cond;

    public IcmpInstr(String name, IcmpCond cond, Value operand1, Value operand2) {
        super(BaseType.INT1, name, InstrType.ICMP);
        this.cond = cond;
        addOperand(operand1);
        addOperand(operand2);
    }

    public String toString() {
        return name +
                " = icmp " +
                cond.toString().toLowerCase() +
                " i32 " +
                operands.get(0).getName() +
                ", " +
                operands.get(1).getName();
    }
}
