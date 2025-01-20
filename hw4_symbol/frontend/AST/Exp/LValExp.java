package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.Token;
import frontend.symbol.ConstSymbol;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.utils.Printer;

import java.util.ArrayList;

public class LValExp extends Exp {
    //LVal → Ident ['[' Exp ']']
    private TokenNode ident;

    public LValExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        ident = (TokenNode) children.get(0);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ident.getValueType();
        } else {
            ValueType vt = ident.getValueType();
            if (vt == ValueType.INT_ARRAY) {
                return ValueType.INT;
            } else if (vt == ValueType.CHAR_ARRAY) {
                return ValueType.CHAR;
            }
        }
        return null;
    }

    public boolean isConst() {
        Symbol symbol = SymbolStack.getInstance().lookupSymbol(ident.getToken().getValue());
        return symbol instanceof ConstSymbol;
    }

    public TokenNode getIdent() {
        return ident;
    }

    @Override
    public void checkError() {
        //先处理本层的error
        Token id = ident.getToken();
        if (SymbolStack.getInstance().lookupSymbol(id.getValue()) == null) {
            Printer.addError("c", id.getLine());
        }
        //再处理子节点的error
        super.checkError();
    }
}
