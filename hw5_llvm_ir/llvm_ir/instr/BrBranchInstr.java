package llvm_ir.instr;

import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

public class BrBranchInstr extends Instr {
    public BrBranchInstr(String name, Value cond, BasicBlock trueBlock, BasicBlock falseBlock) {
        super(BaseType.VOID, name, InstrType.BRANCH);
        addOperand(cond);
        addOperand(trueBlock);
        addOperand(falseBlock);
    }

    public Value getCond() {
        return operands.get(0);
    }

    public BasicBlock getTrueBlock() {
        return (BasicBlock) operands.get(1);
    }

    public BasicBlock getFalseBlock() {
        return (BasicBlock) operands.get(2);
    }

    public String toString() {
        return "br i1 " +
                getCond().getName() +
                ", label %" +
                getTrueBlock().getName() +
                ", label %" +
                getFalseBlock().getName();
    }


}
