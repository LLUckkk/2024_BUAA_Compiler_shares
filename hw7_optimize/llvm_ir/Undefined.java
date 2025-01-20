package llvm_ir;

import llvm_ir.type.LLVMType;

public class Undefined extends Value {
    public Undefined(LLVMType type) {
        super(type, "undefined");
    }

    public int getValue() {
        return 0;
    }

    public String toString() {
        return getName();
    }
}
