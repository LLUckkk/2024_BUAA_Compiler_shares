package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class ConstInitVal extends Node {
    public ConstInitVal(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
