package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Module;
import llvm_ir.instr.*;

import java.util.Iterator;
import java.util.LinkedList;

public class RemoveDeadCode {
    private Module module;

    public RemoveDeadCode(Module module) {
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
                    if (instr.willUse() && (!(instr instanceof CallInstr)) && !isIO(instr)) {
                        if (instr.getUseList().isEmpty()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    private boolean isIO(Instr instr) {
        return instr instanceof PutIntInstr || instr instanceof PutStrInstr || instr instanceof PutCharInstr
                || instr instanceof GetCharInstr || instr instanceof GetIntInstr;
    }
}
