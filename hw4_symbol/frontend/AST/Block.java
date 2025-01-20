package frontend.AST;

import frontend.symbol.SymbolStack;

import java.util.ArrayList;

public class Block extends Node {
    public Block(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        SymbolStack.getInstance().enterBlock();
        super.checkError();
        SymbolStack.getInstance().exitBlock();
    }
}
