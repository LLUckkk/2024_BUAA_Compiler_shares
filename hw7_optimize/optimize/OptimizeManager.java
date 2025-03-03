package optimize;

import llvm_ir.Module;
import utils.Printer;

public class OptimizeManager {
    private static final OptimizeManager optimizeManager = new OptimizeManager();

    public static OptimizeManager getInstance() {
        return optimizeManager;
    }

    public void optimize(Module module) {
        //简化基本块，去除一些永远不可能执行的指令和基本块
        new SimplifyBB(module).optimize();
        Printer.printOriginLLVM(module);
        //构建流图
        new CFG(module).optimize();
        //SSA形式
        new Mem2Reg(module).optimize();

        //活跃变量分析
        new ActiveAnalysis(module).optimize();
        //寄存器分配
        new DistributeReg(module).optimize();
        Printer.printPhiLLVM(module);

        //去除phi指令
        new RemovePhi(module).optimize();
        Printer.printMoveLLVM(module);
    }
}
