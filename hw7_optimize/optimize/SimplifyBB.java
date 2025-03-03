package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Module;
import llvm_ir.instr.BrBranchInstr;
import llvm_ir.instr.BrJumpInstr;
import llvm_ir.instr.ReturnInstr;

import java.util.HashSet;
import java.util.Iterator;

public class SimplifyBB {
    private Module module;

    public SimplifyBB(Module module) {
        this.module = module;
    }

    public void optimize() {
        for (Function f : module.getFunctions()) {
            for (BasicBlock block : f.getBlocks()) {
                removeBr(block);
            }
            removeUnreachBlocks(f);
        }
    }

    private void removeBr(BasicBlock block) {
        boolean canRemove = false;
        Iterator<Instr> iterator = block.getInstrList().iterator();
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            if (canRemove) {
                iterator.remove();
            } else if (instr instanceof BrJumpInstr || instr instanceof BrBranchInstr
                    || instr instanceof ReturnInstr) {
                canRemove = true;
            }
        }
    }

    private void removeUnreachBlocks(Function f) {
        BasicBlock entry = f.getBlocks().get(0);
        HashSet<BasicBlock> visited = new HashSet<>();
        DFS(entry, visited);
        Iterator<BasicBlock> iterator = f.getBlocks().iterator();
        while (iterator.hasNext()) {
            BasicBlock block = iterator.next();
            if (!visited.contains(block)) {
                iterator.remove();
                block.setRemoved();
            }
        }
    }

    private void DFS(BasicBlock block, HashSet<BasicBlock> visited) {
        visited.add(block);
        Instr instr = block.getLastInstr();
        if (instr instanceof BrBranchInstr) {
            BasicBlock trueBlock = ((BrBranchInstr) instr).getTrueBlock();
            BasicBlock falseBlock = ((BrBranchInstr) instr).getFalseBlock();
            if (!visited.contains(trueBlock)) {
                DFS(trueBlock, visited);
            }
            if (!visited.contains(falseBlock)) {
                DFS(falseBlock, visited);
            }
        } else if (instr instanceof BrJumpInstr) {
            BasicBlock dest = ((BrJumpInstr) instr).getDestBlock();
            if (!visited.contains(dest)) {
                DFS(dest, visited);
            }
        }
    }
}
