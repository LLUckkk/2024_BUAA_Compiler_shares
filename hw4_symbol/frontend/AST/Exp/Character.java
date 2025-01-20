package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class Character extends Exp {
    public Character(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        return ValueType.CHAR;
    }
}
