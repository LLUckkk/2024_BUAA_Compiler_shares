package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Module;
import llvm_ir.instr.CallInstr;
import llvm_ir.instr.GetCharInstr;
import llvm_ir.instr.GetIntInstr;

import java.util.Iterator;
import java.util.LinkedList;

public class DeadCodeEliminate {
    private Module module;

    public DeadCodeEliminate(Module module) {
        this.module = module;
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            LinkedList<BasicBlock> blocks = f.getBlocks();
            for (BasicBlock b : blocks) {
                Iterator<Instr> iterator = b.getInstrList().iterator();
                while (iterator.hasNext()) {
                    Instr instr = iterator.next();
                    if (instr.willUse() && (!(instr instanceof CallInstr))
                            && !(instr instanceof GetIntInstr) && !(instr instanceof GetCharInstr)) {
                        if (instr.getUseList().isEmpty()) {
                            iterator.remove();
                        }
                    } //这个地方的逻辑是：可能背删掉的他的instr才是true，但是不会被删掉的都是false
                }
            }
        }
    }
}
