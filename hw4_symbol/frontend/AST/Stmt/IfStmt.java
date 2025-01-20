package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    public IfStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
