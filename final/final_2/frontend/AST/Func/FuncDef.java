package frontend.AST.Func;

import frontend.AST.Block;
import frontend.AST.Node;
import frontend.AST.Stmt.BlockItem;
import frontend.AST.Stmt.ReturnStmt;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.ReturnInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import utils.Printer;

import java.util.ArrayList;

public class FuncDef extends Node {
    //文法：FuncType Ident '(' [FuncFParams] ')' Block
    private FuncSymbol funcSymbol;

    public FuncDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public FuncSymbol createFuncSymbol() {
        ValueType returnType = ((FuncType) children.get(0)).getFuncType();
        String symbolName = ((TokenNode) children.get(1)).getToken().getValue();
        if (children.size() >= 4 && children.get(3) instanceof TokenNode temp) {
            if (temp.getToken().getType() == TokenType.RPARENT) {
                return new FuncSymbol(symbolName, returnType);
            } //没有参数
        } else if (children.size() >= 4 && children.get(3) instanceof FuncFParams funcFParams) {
            ArrayList<ValueType> paramTypes = funcFParams.getFParamsType();
            return new FuncSymbol(symbolName, returnType, paramTypes);
        } else if (children.size() >= 4 && children.get(3) instanceof Block) {
            return new FuncSymbol(symbolName, returnType);
        }
        return null;
    }

    public void checkError() {
        SymbolStack.getInstance().setGlobal(false);
        this.funcSymbol = createFuncSymbol();
        //b:重复定义
        if (!SymbolStack.getInstance().addSymbol(funcSymbol)) {
            int errorLine = ((TokenNode) children.get(1)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
        //symbolStack进入函数
        SymbolStack.getInstance().enterFunc(funcSymbol);
        super.checkError(); //TODO:此处可能有错，随后debug
        SymbolStack.getInstance().exitFunc();
        //离开函数
        //g:如果是有返回值的函数，则最后一句必须是return语句
        Node block = children.get(children.size() - 1);
        ArrayList<Node> blockChildren = block.getChildren();
        TokenNode lastBrace = (TokenNode) blockChildren.get(blockChildren.size() - 1);
        if (blockChildren.size() <= 2) {
            if (!(funcSymbol.getReturnType() == ValueType.VOID)) {
                Printer.addError("g", lastBrace.getToken().getLine());
            }
        } else {
            Node lastSentence = blockChildren.get(blockChildren.size() - 2).getChildren().get(0); //注意没有语句的情况！！！
            if ((!(lastSentence instanceof ReturnStmt)) && funcSymbol.getReturnType() != ValueType.VOID) {
                Printer.addError("g", lastBrace.getToken().getLine());
                //System.out.println("return error in function: " + funcSymbol.getSymbolName());
            }
        }
    }

    public Value genIR() {
        SymbolStack.getInstance().setGlobal(false);
        SymbolStack.getInstance().addSymbol(funcSymbol);
        SymbolStack.getInstance().enterFunc(funcSymbol);
        String funcName = LLVMManager.getInstance().genFunctionName(funcSymbol.getSymbolName());
        LLVMType retType;
        if (funcSymbol.getReturnType() == ValueType.VOID) {
            retType = BaseType.VOID;
        } else if (funcSymbol.getReturnType() == ValueType.INT) {
            retType = BaseType.INT32;
        } else {
            retType = BaseType.CHAR8;
        }
        Function function = new Function(funcName, retType);
        LLVMManager.getInstance().setCurFunc(function);
        funcSymbol.setLlvmFunc(function);
        BasicBlock funcBlock = new BasicBlock(LLVMManager.getInstance().genBlockName());
        LLVMManager.getInstance().setCurBlock(funcBlock);
        super.genIR();
        //此处没写必须保证有一个return
        Block block = (Block) children.get(children.size() - 1);
        ArrayList<Node> blockItems = block.getChildren();
        boolean hasReturn = false;
        for (Node item : blockItems) {
            if (item instanceof BlockItem) {
                if (item.getChildren().get(0) instanceof ReturnStmt) {
                    hasReturn = true;
                }
            }
        }
        if (!hasReturn) {
            new ReturnInstr(LLVMManager.getInstance().genLocalVarName(), null);
        }
        SymbolStack.getInstance().exitFunc();
        return null;
    }
}
