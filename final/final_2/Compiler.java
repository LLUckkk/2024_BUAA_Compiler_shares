import frontend.AST.CompUnit;
import frontend.lexer.TokenLexer;
import frontend.parser.Parser;
import llvm_ir.LLVMManager;
import llvm_ir.Module;
import mips.AssemblyTable;
import mips.MipsManager;
import optimize.OptimizeManager;
import utils.Printer;

public class Compiler {
    public static void main(String[] args) {
        //词法分析
        TokenLexer lexer = new TokenLexer();
        lexer.paresToken(); //此时将文件内容解析为Token
        //语法分析
        Parser parser = new Parser(lexer.getTokenList());
        CompUnit compUnit = (CompUnit) parser.parseCompUnit(); //此时将Token解析为AST
        //进行所有错误类型的检查,包含符号表的构建
        compUnit.checkError();
        //打印错误信息
        Printer.printError();
        //如果没有错误,则进行后续的编译过程
        if (Printer.noError()) {
            //生成中间代码LLVM IR
            LLVMManager.mode = LLVMManager.AUTO_INSERT_MODE;
            compUnit.genIR();
            Module module = LLVMManager.getInstance().getModule();
            //Printer.printLLVM(module); //未优化的中间代码
            //优化部分
            OptimizeManager optimizer = OptimizeManager.getInstance();
            optimizer.optimize(module);
            //生成目标代码mips
            module.toMips();
            AssemblyTable assemblyTable = MipsManager.getInstance().getAssemblyTable();
            Printer.printMips(assemblyTable);
        }
    }
}