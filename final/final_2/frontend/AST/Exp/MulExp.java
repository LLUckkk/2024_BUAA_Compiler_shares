package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.ValueType;
import llvm_ir.Instr;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.CalcInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

public class MulExp extends Exp {
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    public MulExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ((UnaryExp) children.get(0)).getValueType();
        } else {
            return ValueType.INT;
        }
    }

    public int execute() {
        int res = children.get(0).execute();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            Node child = children.get(i);
            if (child instanceof TokenNode) {
                if (((TokenNode) child).getToken().getType() == TokenType.MULT) {
                    res *= children.get(i + 1).execute(); //此处i+1不会超范围
                    i++; //已经计算完了，跳过
                } else if (((TokenNode) child).getToken().getType() == TokenType.DIV) {
                    res /= children.get(i + 1).execute(); //此处i+1不会超范围
                    i++; //已经计算完了，跳过
                } else if (((TokenNode) child).getToken().getType() == TokenType.BITAND) {
                    res &= children.get(i + 1).execute();
                    i++;
                } else {
                    res %= children.get(i + 1).execute(); //此处i+1不会超范围
                    i++; //已经计算完了，跳过
                }
            }
        }
        return res;
    }

    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    public Value genIR() {
        Value operand1 = children.get(0).genIR();
        if (children.size() > 1) {
            if (operand1.getType().isChar8() || operand1.getType() == BaseType.INT1) {
                operand1 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand1, BaseType.INT32);
            }
        }
        Value operand2;
        Instr instr;
        for (int i = 1; i < children.size(); i++) {
            if (children.get(i) instanceof TokenNode) {
                operand2 = children.get(1 + i).genIR();
                if (!operand2.getType().isInt32()) {
                    operand2 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand2, BaseType.INT32);
                }
                if (((TokenNode) children.get(i)).getToken().getType() == TokenType.MULT) {
                    instr = new CalcInstr(LLVMManager.getInstance().genLocalVarName(),
                            CalcInstr.Op.MUL, operand1, operand2);
                } else if (((TokenNode) children.get(i)).getToken().getType() == TokenType.DIV) {
                    instr = new CalcInstr(LLVMManager.getInstance().genLocalVarName(),
                            CalcInstr.Op.SDIV, operand1, operand2);
                } else if (((TokenNode) children.get(i)).getToken().getType() == TokenType.BITAND) {
                    instr = new CalcInstr(LLVMManager.getInstance().genLocalVarName(),
                            CalcInstr.Op.BITAND, operand1, operand2);
                } else {
                    instr = new CalcInstr(LLVMManager.getInstance().genLocalVarName(),
                            CalcInstr.Op.SREM, operand1, operand2);
                }
                operand1 = instr;
            }
        }
        return operand1;
    }
}
