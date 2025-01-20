package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.ValueType;

import java.util.ArrayList;

public class FuncType extends Node {
    public FuncType(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getFuncType() {
        TokenNode funcType = (TokenNode) children.get(0);
        TokenType type = funcType.getToken().getType();
        if (type == TokenType.INTTK) {
            return ValueType.INT;
        } else if (type == TokenType.CHARTK){
            return ValueType.CHAR;
        } else if(type == TokenType.VOIDTK){
            return ValueType.VOID;
        }
        return null;
    }
}
