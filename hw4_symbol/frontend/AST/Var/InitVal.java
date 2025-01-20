package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class InitVal extends Node {
    public InitVal(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
