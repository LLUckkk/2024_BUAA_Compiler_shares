package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class LOrExp extends Exp{
    // LOrExp → LAndExp | LOrExp '||' LAndExp
    public LOrExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
