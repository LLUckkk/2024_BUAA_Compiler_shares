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

    public boolean willUse(){
        return true;
    }

    public String toString() {
        //  %4 = phi i32 [ 1, %2 ], [ %6, %5 ]
        ArrayList<Value> options = getOptions();
        StringBuilder result = new StringBuilder();
        result.append(name).append(" = phi ").append(type).append(" ");
        for (int i = 0; i < foreBlocks.size(); i++) {
            BasicBlock bb = foreBlocks.get(i);
            Value option = options.get(i);
            // 将每个选项格式化为 "[ value, %basicBlock ]" 的形式
            result.append("[ ").append(option.getName()).append(", %").append(bb.getName()).append(" ]");
            // 如果不是最后一个元素，则添加逗号
            if (i < foreBlocks.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
