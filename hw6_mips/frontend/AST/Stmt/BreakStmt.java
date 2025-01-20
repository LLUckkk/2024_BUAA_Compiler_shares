package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import utils.Printer;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.BrJumpInstr;

import java.util.ArrayList;

public class BreakStmt extends Stmt {
    // 'break' ';'
    public BreakStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        if (!SymbolStack.getInstance().isInLoop()) {
            TokenNode tokenNode = (TokenNode) children.get(0);
            Printer.addError("m", tokenNode.getToken().getLine());
        }
        super.checkError();
    }

    public Value genIR() {
        new BrJumpInstr("", LLVMManager.getInstance().getCurLoop().getFollowingBlock());
        return null;
        //为啥这个地方返回null而不是instr,因为是void型语句，没有返回值
    }
}
