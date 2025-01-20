package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class FuncFParams extends Node {
    //文法：FuncFParams → FuncFParam { ',' FuncFParam }
    public FuncFParams(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ArrayList<ValueType> getFParamsType() {
        ArrayList<ValueType> FParamsType = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof FuncFParam) {
                FParamsType.add(((FuncFParam) child).getFParamType());
            }
        }
        return FParamsType;
    }
}
