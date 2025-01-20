package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.ValueType;
import llvm_ir.Constant;
import llvm_ir.Value;

import java.util.ArrayList;

public class Character extends Exp {
    public Character(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        return ValueType.CHAR;
    }

    public int execute() {
        TokenNode token = (TokenNode) children.get(0);
        if (token.getToken().getValue().length() > 3) {
            String value = token.getToken().getValue();
            if (value.charAt(2) == 'a') {
                return 7;
            } else if (value.charAt(2) == 'b') {
                return 8;
            } else if (value.charAt(2) == 't') {
                return 9;
            } else if (value.charAt(2) == 'n') {
                return 10;
            } else if (value.charAt(2) == 'v') {
                return 11;
            } else if (value.charAt(2) == 'f') {
                return 12;
            } else if (value.charAt(2) == '\"') {
                return 34;
            } else if (value.charAt(2) == '\'') {
                return 39;
            } else if (value.charAt(2) == '\\') {
                return 92;
            } else if (value.charAt(2) == '0') {
                return 0;
            }
        } else {
            return ((TokenNode) children.get(0)).getToken().getValue().charAt(1);
        }
        return 0;
    }

    public Value genIR() {
        String ch = ((TokenNode) children.get(0)).getToken().getValue();
        return new Constant(ch);
    }
}
