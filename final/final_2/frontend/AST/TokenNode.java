package frontend.AST;

import frontend.lexer.Token;
import frontend.symbol.*;

public class TokenNode extends Node {
    public TokenNode(Token token) {
        super(token);
    }

    public Token getToken() {
        return this.token;
    }

    public ValueType getValueType() {
        //这个地方需要查表,返回其valuetype
        String name = token.getValue();
        Symbol symbol = SymbolStack.getInstance().lookupSymbol(name);
        if (symbol != null) {
            if (symbol instanceof VarSymbol) {
                return ((VarSymbol) symbol).getValueType();
            } else if (symbol instanceof ConstSymbol) {
                return ((ConstSymbol) symbol).getValueType();
            } else {
                return ((FuncSymbol) symbol).getReturnType();
            }
        }
        return null;
    }
}
