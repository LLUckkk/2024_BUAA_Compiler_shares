package llvm_ir.instr;

import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.BranchAsmb;
import mips.assembly.JumpAsmb;
import mips.assembly.MemoryAsmb;

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

    public void toMips() {
        super.toMips();
        Value cond = getCond();
        BasicBlock trueBlock = getTrueBlock();
        BasicBlock falseBlock = getFalseBlock();

        Register condReg = MipsManager.getInstance().findReg(cond);
        if (condReg == null) {
            condReg = Register.K0;
            int offset = MipsManager.getInstance().findOffset(cond);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, condReg);
        }
        new BranchAsmb(BranchAsmb.BrOp.BNE, condReg, Register.ZERO, trueBlock.getName());
        new JumpAsmb(JumpAsmb.JumpOp.J, falseBlock.getName());
    }


}
