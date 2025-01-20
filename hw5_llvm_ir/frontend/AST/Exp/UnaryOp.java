package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class UnaryOp extends Exp {
    public UnaryOp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
