package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import llvm_ir.BasicBlock;

import java.util.ArrayList;

public class Cond extends Exp {
    // Cond → LOrExp
    public Cond(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void genIRForCond(BasicBlock thenBlock, BasicBlock elseBlock) {
        ((LOrExp) children.get(0)).genIRForOr(thenBlock, elseBlock);
    }
}
