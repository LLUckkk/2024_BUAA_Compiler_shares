package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class MulExp extends Exp {
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    public MulExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ((UnaryExp) children.get(0)).getValueType();
        } else {
            return ValueType.INT;
        }
    }
}
