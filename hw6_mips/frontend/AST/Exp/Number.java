package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.ValueType;
import llvm_ir.Constant;
import llvm_ir.Value;

import java.util.ArrayList;

public class Number extends Exp {
    public Number(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        return ValueType.INT;
    }

    public Value genIR() {
        String num = ((TokenNode) children.get(0)).getToken().getValue();
        return new Constant(Integer.parseInt(num));
    }

    public int execute() {
        return Integer.parseInt(((TokenNode) children.get(0)).getToken().getValue());
    }
}
