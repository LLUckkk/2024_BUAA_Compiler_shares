package optimize;

import llvm_ir.Module;
import utils.Printer;

public class OptimizeManager {
    private static final OptimizeManager optimizeManager = new OptimizeManager();

    public static OptimizeManager getInstance() {
        return optimizeManager;
    }

    public void optimize(Module module) {
        new SimplifyBB(module).optimize();
        Printer.printOriginLLVM(module);
        new CFG(module).optimize();

        new Mem2Reg(module).optimize();


        new ActiveAnalysis(module).optimize();
        new DistributeReg(module).optimize();

        Printer.printPhiLLVM(module);

        new EliminatePhi(module).optimize();
        Printer.printMoveLLVM(module);
    }
}
