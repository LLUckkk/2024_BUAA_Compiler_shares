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
        SymbolStack.getInstance().enterBlock();
        super.checkError();
        SymbolStack.getInstance().exitBlock();
    }
}
