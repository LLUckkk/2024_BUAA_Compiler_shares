package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import llvm_ir.BasicBlock;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.BrBranchInstr;

import java.util.ArrayList;

public class LAndExp extends Exp {
    // LAndExp → EqExp | LAndExp '&&' EqExp
    public LAndExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void genIRForAnd(BasicBlock thenBlock, BasicBlock elseBlock) {
        for (Node child : children) {
            if (child instanceof EqExp) {
                if (children.indexOf(child) == children.size() - 1) {
                    Value cond = child.genIR();
                    new BrBranchInstr("", cond, thenBlock, elseBlock);
                } else {
                    BasicBlock next = new BasicBlock(LLVMManager.getInstance().genBlockName());
                    Value cond = child.genIR();
                    new BrBranchInstr("", cond, next, elseBlock);
                    LLVMManager.getInstance().setCurBlock(next);
                }
            }
        }
    }
}
