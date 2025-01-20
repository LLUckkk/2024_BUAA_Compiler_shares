package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.ValueType;
import llvm_ir.Value;

import java.util.ArrayList;

public class PrimaryExp extends Exp {
    //PrimaryExp → '(' Exp ')' | LVal | Number | Character
    public PrimaryExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            if (children.get(0) instanceof LValExp lValExp) {
                return lValExp.getValueType();
            } else if (children.get(0) instanceof Number number) {
                return number.getValueType();
            } else if (children.get(0) instanceof Character character) {
                return character.getValueType();
            }
        } else {
            return ((Exp) children.get(1)).getValueType();
        }
        return null;
    }

    //PrimaryExp → '(' Exp ')' | LVal | Number | Character
    public int execute() {
        if (children.get(0) instanceof Number) {
            return children.get(0).execute();
        } else if (children.get(0) instanceof Character) {
            return children.get(0).execute();
        } else if (children.get(0) instanceof LValExp) {
            return children.get(0).execute();
        } else {
            return children.get(1).execute();
        }
    }

    //PrimaryExp → '(' Exp ')' | LVal | Number | Character
    public Value genIR() {
        if (children.get(0) instanceof TokenNode) {
            return children.get(1).genIR();
        } else {
            return children.get(0).genIR();
        }
    }
}
