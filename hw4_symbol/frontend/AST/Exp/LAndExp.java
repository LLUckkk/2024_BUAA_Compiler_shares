package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class LAndExp extends Exp{
    // LAndExp → EqExp | LAndExp '&&' EqExp
    public LAndExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
