package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.symbol.VarSymbol;
import frontend.utils.Printer;

import java.util.ArrayList;

public class FuncFParam extends Node {
    //文法：FuncFParam → BType Ident ['[' ']']
    private VarSymbol varSymbol;

    public FuncFParam(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public VarSymbol createVarSymbol() {
        TokenNode bType = (TokenNode) children.get(0);
        TokenNode ident = (TokenNode) children.get(1);
        String symbolName = ident.getToken().getValue();
        TokenType bTypeToken = bType.getToken().getType();
        ValueType valueType;
        if (children.size() == 2) {
            if (bTypeToken == TokenType.INTTK) {
                valueType = ValueType.INT;
            } else {
                valueType = ValueType.CHAR;
            }
        } else {
            if (bTypeToken == TokenType.INTTK) {
                valueType = ValueType.INT_ARRAY;
            } else {
                valueType = ValueType.CHAR_ARRAY;
            }
        }
        return new VarSymbol(symbolName, valueType);
    }

    public void checkError() {
        this.varSymbol = createVarSymbol();
        //b:重复定义
        if (!SymbolStack.getInstance().addSymbol(varSymbol)) {
            int errorLine = ((TokenNode) children.get(1)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
        super.checkError();
    }

    public ValueType getFParamType() {
        if (varSymbol == null) {
            TokenNode bType = (TokenNode) children.get(0);
            TokenType bTypeToken = bType.getToken().getType();
            if (children.size() == 2) {
                if (bTypeToken == TokenType.INTTK) {
                    return ValueType.INT;
                } else {
                    return ValueType.CHAR;
                }
            } else {
                if (bTypeToken == TokenType.INTTK) {
                    return ValueType.INT_ARRAY;
                } else {
                    return ValueType.CHAR_ARRAY;
                }
            }
        } else {
            return varSymbol.getValueType();
        }
    }
}
