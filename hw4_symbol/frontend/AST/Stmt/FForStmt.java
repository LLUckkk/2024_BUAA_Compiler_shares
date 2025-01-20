package frontend.AST.Stmt;

import frontend.AST.Block;
import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import frontend.utils.Printer;

import java.util.ArrayList;

public class FForStmt extends Stmt {
    // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    public FForStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError(){
        SymbolStack.getInstance().enterLoop();
        super.checkError();
        SymbolStack.getInstance().exitLoop();
    } //对于if和for，也可以有return，需要判断其所在的函数的返回值类型
}
