package llvm_ir;

import llvm_ir.type.LLVMType;

public class Instr extends User {
    private InstrType instrType;
    private BasicBlock parent;

    public Instr(LLVMType type, String name, InstrType instrType) {
        super(type, name);
        this.instrType = instrType;
        this.parent = null;
        //IRBuilder的mode还没太懂是什么意
        IRBuilder.getInstance().addInstr(this);
    }

    public BasicBlock getParent() {
        return parent;
    }

    public void setParent(BasicBlock parent) {
        this.parent = parent;
    }

    public void toMips() {

    }
}
