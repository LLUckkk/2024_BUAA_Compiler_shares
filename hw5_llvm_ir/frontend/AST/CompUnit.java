package frontend.AST;

import frontend.symbol.SymbolStack;
import llvm_ir.Value;

import java.util.ArrayList;

public class CompUnit extends Node {
    public CompUnit(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    @Override
    public void checkError() {
        SymbolStack.getInstance().setGlobal(true);
        SymbolStack.getInstance().enterBlock();
        super.checkError();
        SymbolStack.getInstance().exitBlock();
    }

    public Value genIR(){
        //set global 表明之后定义的变量可能是全局的，如果有定义需要计算出初始值
        SymbolStack.getInstance().setGlobal(true);
        SymbolStack.getInstance().enterBlock();
        super.genIR();
        SymbolStack.getInstance().exitBlock();
        return null;
    }
}
