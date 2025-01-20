package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class ConstExp extends Exp {
    // ConstExp → AddExp
    public ConstExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
