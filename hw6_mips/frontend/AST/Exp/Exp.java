package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.ValueType;
import llvm_ir.Value;

import java.util.ArrayList;

public class Exp extends Node {
    // Exp → AddExp
    public Exp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        AddExp addExp = (AddExp) children.get(0);
        return addExp.getValueType();
    }

    public int execute(){
        return children.get(0).execute();
    }

    public Value genIR(){
        return children.get(0).genIR();
    }
}
