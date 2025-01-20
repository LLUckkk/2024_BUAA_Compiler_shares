package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class CalcInstr extends Instr {
    public enum Op {
        ADD, SUB, MUL, SDIV, SREM
    }

    private Op op;

    public CalcInstr(String name, Op op, Value operand1, Value operand2) {
        super(BaseType.INT32, name, InstrType.CALC);
        this.op = op;
        addOperand(operand1);
        addOperand(operand2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = ");
        sb.append(op.toString().toLowerCase());
        sb.append(" i32 ");
        sb.append(operands.get(0).getName());
        sb.append(", ");
        sb.append(operands.get(1).getName());
        return sb.toString();
    }
}
