package llvm_ir;

import llvm_ir.type.LLVMType;

public class Param extends Value {
    private Function belong;

    public Param(LLVMType type, String name) {
        super(type, name);
        if (LLVMManager.mode == LLVMManager.DEFAULT_MODE) {
            LLVMManager.getInstance().addParam(this);
        }
        belong = null;
    }

    public void setBelong(Function belong) {
        this.belong = belong;
    }

    public String toString() {
        return type.toString() + " " + name;
    }
}
