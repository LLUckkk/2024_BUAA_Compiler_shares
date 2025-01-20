package llvm_ir;

import llvm_ir.type.LLVMType;

import java.util.ArrayList;

public class User extends Value {
    public ArrayList<Value> operands;

    public User(LLVMType type, String name) {
        super(type, name);
        operands = new ArrayList<>();
    }

    public void addOperand(Value operand) {
        operands.add(operand);
        if (operand != null) {
            operand.addUse(this);
        }
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }

}
