package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.Stmt.ReturnStmt;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.utils.Printer;

import java.util.ArrayList;

public class MainFuncDef extends Node {
    //'int' 'main' '(' ')' Block
    private FuncSymbol mainFuncSymbol;

    public MainFuncDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

//    public FuncSymbol createMainFuncSymbol() {
//        return new FuncSymbol("main", ValueType.INT);
//    }

    public void checkError() {
        // this.mainFuncSymbol = createMainFuncSymbol();
        //b:重复定义
//        if (!SymbolStack.getInstance().addSymbol(mainFuncSymbol)) {
//            int errorLine = ((TokenNode) children.get(1)).getToken().getLine();
//            Printer.addError("b", errorLine);
//        }
        //enterFunc
        SymbolStack.getInstance().enterFunc(mainFuncSymbol);
        super.checkError(); //TODO:此处可能不对
        SymbolStack.getInstance().exitFunc();
        //g:没有返回语句
        Node block = children.get(children.size() - 1);
        ArrayList<Node> blockChildren = block.getChildren();
        TokenNode lastBrace = (TokenNode) blockChildren.get(blockChildren.size() - 1);
        if (blockChildren.size() <= 2) {
            Printer.addError("g", lastBrace.getToken().getLine());
        } else {
            Node lastSentence = blockChildren.get(blockChildren.size() - 2).getChildren().get(0);
            if (!(lastSentence instanceof ReturnStmt)) {
                Printer.addError("g", lastBrace.getToken().getLine());
                //System.out.println("main return error");
            }
        }
    }

}
