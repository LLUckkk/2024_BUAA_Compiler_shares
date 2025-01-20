package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class EqExp extends Exp {
    //EqExp → RelExp | EqExp ('==' | '!=') RelExp
    public EqExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
