package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class Stmt extends Node {
    public Stmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
