package llvm_ir;

import llvm_ir.type.LLVMType;
import mips.assembly.CommentAsmb;

public class Instr extends User {
    private InstrType instrType;
    private BasicBlock parent;

    public Instr(LLVMType type, String name, InstrType instrType) {
        super(type, name);
        this.instrType = instrType;
        this.parent = null;
        //IRBuilder的mode还没太懂是什么意
        if(LLVMManager.mode == LLVMManager.AUTO_INSERT_MODE){
            LLVMManager.getInstance().addInstr(this);
        }
    }

    public BasicBlock getParentBlock() {
        return parent;
    }


    public void setParent(BasicBlock parent) {
        this.parent = parent;
    }

    public void toMips() {
        new CommentAsmb("\n# " + this.toString());
    }

    public boolean willUse() {
        return false;
    }
}
