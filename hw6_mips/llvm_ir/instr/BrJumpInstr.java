package llvm_ir.instr;

import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.type.BaseType;
import mips.assembly.JumpAsmb;

public class BrJumpInstr extends Instr {

    public BrJumpInstr(String name, BasicBlock destBlock) {
        super(BaseType.VOID, name, InstrType.JUMP);
        addOperand(destBlock);
    }

    public BasicBlock getDestBlock() {
        return (BasicBlock) operands.get(0);
    }

    public String toString() {
        return "br label %" +
                getDestBlock().getName();
    }

    public void toMips() {
        super.toMips();
        new JumpAsmb(JumpAsmb.JumpOp.J, getDestBlock().getName());
    }
}
