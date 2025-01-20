package frontend.AST.Stmt;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.BrJumpInstr;
import utils.Printer;

import java.util.ArrayList;

public class ContinueStmt extends Stmt {
    public ContinueStmt(ArrayList<Node> children, SyntaxType type) {
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
        new BrJumpInstr("", LLVMManager.getInstance().getCurLoop().getHeadBlock());
        return null;
    }
}
