package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.ConstSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.utils.Printer;

import java.util.ArrayList;

public class ConstDef extends Node {
    /* constDef的文法: Ident [ '[' ConstExp ']' ] '=' ConstInitVal*/
    private boolean isChar;
    private ConstSymbol constSymbol;

    public ConstDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        isChar = false;
    }

    public void setIsChar() {
        isChar = true;
    }

    public ConstSymbol createSymbol() {
        TokenNode ident = (TokenNode) children.get(0);
        String symbolName = ident.getToken().getValue();
        ValueType valueType;
        if (children.size() == 3) {
            if (isChar) {
                valueType = ValueType.CHAR;
                isChar = false;
            } else {
                valueType = ValueType.INT;
            }
        } else {
            if (isChar) {
                valueType = ValueType.CHAR_ARRAY;
                isChar = false;
            } else {
                valueType = ValueType.INT_ARRAY;
            }
        }
        return new ConstSymbol(symbolName, valueType);
    }


    @Override
    public void checkError() {
        this.constSymbol = createSymbol();
        super.checkError();
        //b:标识符重定义
        if (!SymbolStack.getInstance().addSymbol(constSymbol)) {
            int errorLine = ((TokenNode) children.get(0)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
    }
}
