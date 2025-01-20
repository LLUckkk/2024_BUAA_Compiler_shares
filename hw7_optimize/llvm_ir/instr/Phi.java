package llvm_ir.instr;

import llvm_ir.BasicBlock;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;

import java.util.ArrayList;

public class Phi extends Instr {
    private ArrayList<BasicBlock> foreBlocks;

    public Phi(LLVMType type, String name, ArrayList<BasicBlock> foreBlocks) {
        super(type, name, InstrType.PHI);
        this.foreBlocks = foreBlocks;
        for (BasicBlock bb : foreBlocks) {
            addOperand(null);
        }
    }

    public void addOption(BasicBlock block, Value option) {
        int index = foreBlocks.indexOf(block);
        operands.set(index, option);
        option.addUse(this);
    }

    public ArrayList<Value> getOptions() {
        return operands;
    }
}
