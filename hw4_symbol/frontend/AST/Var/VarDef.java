package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.symbol.VarSymbol;
import frontend.utils.Printer;

import java.util.ArrayList;

public class VarDef extends Node {
    private boolean isChar;
    private VarSymbol varSymbol;

    //文法： VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '='InitVal
    public VarDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        isChar = false;
    }

    public void setIsChar() {
        isChar = true;
    }

    public VarSymbol createVarSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getToken().getValue();
        if (children.size() == 1 || (children.size() == 3 &&
                ((TokenNode) children.get(1)).getToken().getValue().equals("="))) {
            if (isChar) {
                isChar = false;
                return new VarSymbol(symbolName, ValueType.CHAR);
            } else {
                return new VarSymbol(symbolName, ValueType.INT);
            }
        } else {
            if (isChar) {
                isChar = false;
                return new VarSymbol(symbolName, ValueType.CHAR_ARRAY);
            } else {
                return new VarSymbol(symbolName, ValueType.INT_ARRAY);
            }
        }
    }

    public void checkError() {
        this.varSymbol = createVarSymbol();
        super.checkError();
        //b:当前作用域重复定义
        if (!SymbolStack.getInstance().addSymbol(varSymbol)) {
            int errorLine = ((TokenNode) children.get(0)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
    }
}
