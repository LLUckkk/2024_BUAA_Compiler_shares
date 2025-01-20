package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.IcmpInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

public class RelExp extends Exp {
    //RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    public RelExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    //RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    public Value genIR() {
        Value operand1 = children.get(0).genIR(); //AddExp
        Value operand2;
        if (children.size() == 1) {
            return operand1;
        } else {
            for (int i = 1; i < children.size(); i++) {
                if (children.get(i) instanceof TokenNode node) {
                    if (!operand1.getType().isInt32()) {
                        operand1 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand1, BaseType.INT32);
                    } //这里应该是类型转换
                    operand2 = children.get(i + 1).genIR();
                    if (!operand2.getType().isInt32()) {
                        operand2 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand2, BaseType.INT32);
                    }
                    IcmpInstr.IcmpCond cond;
                    TokenType type = node.getToken().getType();
                    if (type == TokenType.GRE) {
                        cond = IcmpInstr.IcmpCond.SGT;
                    } else if (type == TokenType.GEQ) {
                        cond = IcmpInstr.IcmpCond.SGE;
                    } else if (type == TokenType.LSS) {
                        cond = IcmpInstr.IcmpCond.SLT;
                    } else {
                        cond = IcmpInstr.IcmpCond.SLE;
                    }
                    operand1 = new IcmpInstr(LLVMManager.getInstance().genLocalVarName(), cond, operand1, operand2);
                }
            }
            return operand1;
        }
    }
}
