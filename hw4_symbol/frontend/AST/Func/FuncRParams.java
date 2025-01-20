package frontend.AST.Func;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class FuncRParams extends Node {
    private ArrayList<ValueType> params;

    //文法：Exp { ',' Exp }
    public FuncRParams(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        params = new ArrayList<>();
    }

    public void setRParams() {
        for (Node child : children) {
            if (child instanceof Exp) {
                params.add(((Exp) child).getValueType());
            }
        }
    }

    public ArrayList<ValueType> getRParamsType() {
        return params;
    }

    public int getRParamsNum() {
        return params.size();
    }
}
