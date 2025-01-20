package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.SymbolStack;

import java.util.ArrayList;

public class BlockItem extends Stmt {
    public BlockItem(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
