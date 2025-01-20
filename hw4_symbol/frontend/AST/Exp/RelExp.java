package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;

import java.util.ArrayList;

public class RelExp extends Exp{
    //RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    public RelExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }
}
