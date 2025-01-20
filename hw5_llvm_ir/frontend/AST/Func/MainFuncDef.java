package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.Stmt.ReturnStmt;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.FuncSymbol;
import frontend.symbol.Symbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import utils.Printer;
import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

public class MainFuncDef extends Node {
    //'int' 'main' '(' ')' Block
    private FuncSymbol mainFuncSymbol;

    public MainFuncDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public FuncSymbol createMainFuncSymbol() {
        return new FuncSymbol("main", ValueType.INT);
    }

    public void checkError() {
        mainFuncSymbol = createMainFuncSymbol();
        SymbolStack.getInstance().setGlobal(false);
        SymbolStack.getInstance().enterFunc(mainFuncSymbol);
        super.checkError();
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

    public Value genIR() {
        SymbolStack.getInstance().setGlobal(false);
        SymbolStack.getInstance().enterFunc(mainFuncSymbol);
        String name = IRBuilder.getInstance().genFunctionName("main");
        Function function = new Function(name, BaseType.INT32);
        mainFuncSymbol.setLlvmFunc(function);
        IRBuilder.getInstance().setCurFunc(function);
        BasicBlock mainBlock = new BasicBlock(name);
        IRBuilder.getInstance().setCurBlock(mainBlock);
        super.genIR();
        SymbolStack.getInstance().exitFunc();
        return null;
    }

}
