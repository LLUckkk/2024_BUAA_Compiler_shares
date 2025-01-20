package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import frontend.utils.Printer;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    // 'break' ';'
    public BreakStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        if (!SymbolStack.getInstance().isInLoop()) {
            TokenNode tokenNode = (TokenNode) children.get(0);
            Printer.addError("m", tokenNode.getToken().getLine());
        }
        super.checkError();
    }
}
