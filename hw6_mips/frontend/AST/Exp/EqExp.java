package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.IcmpInstr;
import llvm_ir.Constant;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

public class EqExp extends Exp {
    //EqExp → RelExp | EqExp ('==' | '!=') RelExp
    public EqExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public Value genIR() {
        Value operand1 = children.get(0).genIR();
        Value operand2;
        Value ans;
        if (children.size() == 1) {
            if (!operand1.getType().isInt32()) {
                operand1 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand1, BaseType.INT32);
            }
            operand1 = new IcmpInstr(LLVMManager.getInstance().genLocalVarName(),
                    IcmpInstr.IcmpCond.NE, operand1, new Constant(0));
            return operand1;
        } else {
            for (int i = 1; i < children.size(); i++) {
                if (children.get(i) instanceof TokenNode) {
                    if (!operand1.getType().isInt32()) {
                        operand1 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand1, BaseType.INT32);
                    }
                    operand2 = children.get(i + 1).genIR();
                    if (!operand2.getType().isInt32()) {
                        operand2 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand2, BaseType.INT32);
                    }
                    TokenNode node = (TokenNode) children.get(i);
                    if (node.getToken().getType() == TokenType.NEQ) {
                        //！=
                        ans = new IcmpInstr(LLVMManager.getInstance().genLocalVarName(),
                                IcmpInstr.IcmpCond.NE, operand1, operand2);
                    } else {
                        ans = new IcmpInstr(LLVMManager.getInstance().genLocalVarName(),
                                IcmpInstr.IcmpCond.EQ, operand1, operand2);
                    }
                    operand1 = ans;
                }
            }
            return operand1;
        }
    }
}
