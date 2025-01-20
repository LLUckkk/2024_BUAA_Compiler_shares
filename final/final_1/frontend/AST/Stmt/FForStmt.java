package frontend.AST.Stmt;

import frontend.AST.Exp.Cond;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import llvm_ir.*;
import llvm_ir.instr.BrJumpInstr;

import java.util.ArrayList;

public class FForStmt extends Stmt {
    // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    public FForStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        SymbolStack.getInstance().enterLoop();
        SymbolStack.getInstance().enterBlock();
        super.checkError();
        SymbolStack.getInstance().exitBlock();
        SymbolStack.getInstance().exitLoop();
    } //对于if和for，也可以有return，需要判断其所在的函数的返回值类型


    // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    public Value genIR() {
        SymbolStack.getInstance().enterLoop();
        SymbolStack.getInstance().enterBlock();
        BasicBlock cond = null, body = null, following = null, for2 = null;
        int hasCond = -1;
        int hasFor1 = -1;
        int hasFor2 = -1;
        //forStmt1
//        if (children.get(2) instanceof ForStmt) {
//            children.get(2).genIR();
//        }
        int size = children.size();
        //body
        body = new BasicBlock(LLVMManager.getInstance().genBlockName());
        //following
        following = new BasicBlock(LLVMManager.getInstance().genBlockName());
        for (int i = 2; i < size - 1; i++) {
            if (children.get(i) instanceof ForStmt) {
                TokenNode token = (TokenNode) children.get(i + 1);
                if (token.getToken().getValue().equals(";")) {
                    hasFor1 = i;
                } else {
                    hasFor2 = i;
                }
            } else if (children.get(i) instanceof Cond) {
                hasCond = i;
            }
        }
        if (hasFor1 != -1) {
            children.get(hasFor1).genIR();
        }
        if (hasCond != -1) {
            cond = new BasicBlock(LLVMManager.getInstance().genBlockName());
            new BrJumpInstr("", cond);
            LLVMManager.getInstance().setCurBlock(cond);
            ((Cond) children.get(hasCond)).genIRForCond(body, following);
        } else {
            new BrJumpInstr("", body);
        }
        if (hasFor2 != -1) {
            for2 = new BasicBlock(LLVMManager.getInstance().genBlockName());
        }
        //Loop进栈
        if (hasFor2 != -1) {
            LLVMManager.getInstance().pushLoop(new Loop(for2, following, body));
        } else if (hasCond != -1) {
            LLVMManager.getInstance().pushLoop(new Loop(cond, following, body));
        } else {
            LLVMManager.getInstance().pushLoop(new Loop(body, following, body));
        }
        //解析body
        LLVMManager.getInstance().setCurBlock(body);
        children.get(children.size() - 1).genIR();
        if (hasFor2 != -1) {
            new BrJumpInstr("", for2);
            LLVMManager.getInstance().setCurBlock(for2);
            children.get(hasFor2).genIR();
        }
        if (hasCond != -1) {
            new BrJumpInstr("", cond);
        } else {
            new BrJumpInstr("", body);
        }
        //loop出栈
        LLVMManager.getInstance().popLoop();
        LLVMManager.getInstance().setCurBlock(following);
        SymbolStack.getInstance().exitBlock();
        SymbolStack.getInstance().exitLoop();
        return null;
    }
}
