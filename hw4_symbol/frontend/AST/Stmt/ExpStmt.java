package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class ExpStmt extends Stmt {
    public ExpStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
