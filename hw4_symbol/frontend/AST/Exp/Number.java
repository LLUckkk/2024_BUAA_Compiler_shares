package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class Number extends Exp {
    public Number(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        return ValueType.INT;
    }
}
