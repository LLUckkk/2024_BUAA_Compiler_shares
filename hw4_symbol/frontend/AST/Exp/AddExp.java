package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class AddExp extends Exp {
    //AddExp → MulExp | AddExp ('+' | '−') MulExp
    public AddExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ((MulExp) children.get(0)).getValueType();
        } else {
            return ValueType.INT;
        }
    }
}
