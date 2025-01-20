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

    public void modifyOperand(Value oldOperand, Value newOperand) {
        if (!operands.contains(oldOperand)) {
            System.out.println("Error: Trying to modify operand that is not in the list of operands.");
        } else {
            int index = operands.indexOf(oldOperand);
            oldOperand.removeUser(this);
            operands.set(index, newOperand);
            newOperand.addUse(this);
            System.out.println("Modified operand " + oldOperand.getName() + " to " + newOperand.getName());
        }
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }

}
