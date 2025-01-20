package frontend.AST.Stmt;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.utils.Printer;

import java.util.ArrayList;

public class ReturnStmt extends Stmt {
    // 'return' [Exp] ';'
    public ReturnStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError(){
        super.checkError();
        FuncSymbol funcSymbol = SymbolStack.getInstance().getLastFunc();
        if(funcSymbol != null){
            ValueType returnType = funcSymbol.getReturnType();
            if(returnType == ValueType.VOID){
                if(children.size() >= 2 && children.get(1) instanceof Exp){
                    int errorLine = ((TokenNode)children.get(0)).getToken().getLine();
                    Printer.addError("f", errorLine);
                }
            }
        }
    }
}
