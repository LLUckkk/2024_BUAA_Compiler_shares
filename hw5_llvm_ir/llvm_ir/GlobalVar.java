package llvm_ir;

import llvm_ir.initial.Initial;
import llvm_ir.type.LLVMType;

public class GlobalVar extends User {
    private Initial init;

    public GlobalVar(LLVMType type, String name, Initial init) {
        super(type, name);
        this.init = init;
        IRBuilder.getInstance().addGlobalVar(this);
    } //是一个指针变量

    public String toString() {
        return name + " = dso_local global " + init.toString();
    }
}
