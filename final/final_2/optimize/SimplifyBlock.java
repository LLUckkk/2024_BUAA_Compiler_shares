package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Module;
import llvm_ir.instr.BrBranchInstr;
import llvm_ir.instr.BrJumpInstr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class SimplifyBlock {
    private Module module;

    public SimplifyBlock(Module module) {
        this.module = module;
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            LinkedList<BasicBlock> basicBlocks = f.getBlocks();
            for (BasicBlock basicBlock : basicBlocks) {
                removeRedundantBr(basicBlock);
            }
        }

        for (Function f : functions) {
            removeDeadBlocks(f);
        }
    }

    //去掉多余的br语句
    public void removeRedundantBr(BasicBlock basicBlock) {
        Iterator<Instr> iterator = basicBlock.getInstrList().iterator();
        boolean isRedundant = false;
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            if (isRedundant) {
                iterator.remove();
                isRedundant = false; //注意这句学长代码似乎没有
            } else if (instr instanceof BrJumpInstr || instr instanceof BrBranchInstr) {
                isRedundant = true;
            }
        }
    }

    //去掉不可到达的block
    public void removeDeadBlocks(Function f) {
        BasicBlock entryBlock = f.getBlocks().get(0);
        HashSet<BasicBlock> visit = new HashSet<>();
        DFS(entryBlock, visit);
        Iterator<BasicBlock> iterator = f.getBlocks().iterator();
        while (iterator.hasNext()) {
            BasicBlock block = iterator.next();
            if (!visit.contains(block)) {
                iterator.remove();
                block.setRemoved(); //这个地方注意setRemove了
            }
        }
    }

    public void DFS(BasicBlock block, HashSet<BasicBlock> visit) {
        visit.add(block);
        Instr lastInstr = block.getLastInstr();
        if (lastInstr instanceof BrJumpInstr) {
            BasicBlock destBlock = ((BrJumpInstr) lastInstr).getDestBlock();
            DFS(destBlock, visit);
        } else if (lastInstr instanceof BrBranchInstr) {
            BasicBlock trueBlock = ((BrBranchInstr) lastInstr).getTrueBlock();
            BasicBlock falseBlock = ((BrBranchInstr) lastInstr).getFalseBlock();
            DFS(trueBlock, visit);
            DFS(falseBlock, visit);
        }
    }
}
