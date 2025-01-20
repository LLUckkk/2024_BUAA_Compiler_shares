package frontend.AST;

import frontend.symbol.SymbolStack;
import llvm_ir.Value;

import java.util.ArrayList;

public class Block extends Node {
    //Block → '{' { BlockItem } '}'
    public Block(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        SymbolStack.getInstance().enterBlock();
        super.checkError();
        SymbolStack.getInstance().exitBlock();
    }

    public Value genIR() {
        SymbolStack.getInstance().enterBlock();
        super.genIR();
        SymbolStack.getInstance().exitBlock();
        return null;
    }
}
