package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import llvm_ir.BasicBlock;
import llvm_ir.IRBuilder;

import java.util.ArrayList;

public class LOrExp extends Exp {
    // LOrExp → LAndExp | LOrExp '||' LAndExp
    public LOrExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    //这个地方似乎没有短路求值？？
    public void genIRForOr(BasicBlock thenBlock, BasicBlock elseBlock) {
        for (Node child : children) {
            if (child instanceof LAndExp) {
                if (children.indexOf(child) == children.size() - 1) {
                    ((LAndExp) child).genIRForAnd(thenBlock, elseBlock);
                } else {
                    BasicBlock next = new BasicBlock(IRBuilder.getInstance().genBlockName());
                    ((LAndExp) child).genIRForAnd(thenBlock, next);
                    IRBuilder.getInstance().setCurBlock(next);
                }
            }
        }
    }
}
