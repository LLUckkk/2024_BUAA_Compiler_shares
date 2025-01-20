package optimize;

import llvm_ir.Module;
import utils.Printer;

public class OptimizeManager {
    private static final OptimizeManager optimizeManager = new OptimizeManager();

    public static OptimizeManager getInstance() {
        return optimizeManager;
    }

    public void optimize(Module module) {
        //new SimplifyBlock(module).optimize();
        Printer.printLLVM(module);
        //new Mem2Reg().optimize();
        new CFG(module).optimize();
        new ActiveAnalysis(module).optimize();
        new DistributeReg(module).optimize();
        //new DeadCodeEliminate(module).optimize();
        //Printer.printRemoveLLVM(module);
        //new EliminatePhi(module).optimize();
        //Printer.printLLVM(module);
    }
}
