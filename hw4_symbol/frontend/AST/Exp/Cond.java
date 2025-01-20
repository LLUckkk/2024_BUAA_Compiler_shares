package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class Cond extends Exp {
    // Cond → LOrExp
    public Cond(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
