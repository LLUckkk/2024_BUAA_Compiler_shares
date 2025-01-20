package frontend.AST.Stmt;

import frontend.AST.Exp.Cond;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;
import llvm_ir.Instr;
import llvm_ir.Value;
import llvm_ir.instr.BrJumpInstr;

import java.util.ArrayList;

public class IfStmt extends Stmt {
    public IfStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    //'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    public Value genIR() {
        BasicBlock trueBlock = new BasicBlock(IRBuilder.getInstance().genBlockName());
        BasicBlock following = new BasicBlock(IRBuilder.getInstance().genBlockName());
        if (children.size() > 5) {
            //有else分支
            BasicBlock falseBlock = new BasicBlock(IRBuilder.getInstance().genBlockName());
            ((Cond) children.get(2)).genIRForCond(trueBlock, falseBlock);
            IRBuilder.getInstance().setCurBlock(falseBlock);
            children.get(6).genIR();
            new BrJumpInstr("", following);
        } else {
            ((Cond) children.get(2)).genIRForCond(trueBlock, following);
        }
        IRBuilder.getInstance().setCurBlock(trueBlock);
        children.get(4).genIR();
        new BrJumpInstr("", following);
        IRBuilder.getInstance().setCurBlock(following);
        return null;
    }
}
