package frontend.AST.Stmt;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import llvm_ir.instr.*;
import llvm_ir.type.BaseType;
import utils.Printer;
import llvm_ir.LLVMManager;
import llvm_ir.StringLiteral;
import llvm_ir.Value;

import java.util.ArrayList;

public class PrintfStmt extends Stmt {
    //'printf''('StringConst {','Exp}')'';'
    public PrintfStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        //l:格式字符和表达式个数不匹配
        String formatStr = ((TokenNode) children.get(2)).getToken().getValue();
        int cnt_format = 0;
        int cnt_exp = 0;
        for (int i = 0; i < formatStr.length(); i++) {
            if (formatStr.charAt(i) == '%' && (i + 1) < formatStr.length() && (formatStr.charAt(i + 1) == 'd'
                    || formatStr.charAt(i + 1) == 'c')) {
                cnt_format++;
            }
        }
        for (Node child : children) {
            if (child instanceof Exp) {
                cnt_exp++;
            }
        }
        if (cnt_exp != cnt_format) {
            TokenNode token = (TokenNode) children.get(0);
            Printer.addError("l", token.getToken().getLine());
        }
        super.checkError();
    }

    //'printf''('StringConst {','Exp}')'';'
    public Value genIR() {
        ArrayList<Value> expValue = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                expValue.add(child.genIR());
            }
        } //也就是先把这里面的值算完
        //下面进行的是如何把printf用llvm ir实现
        String format = ((TokenNode) children.get(2)).getToken().getValue();
        format = format.substring(1, format.length() - 1); //去掉冒号等等
        StringBuilder sb = new StringBuilder();
        int expCnt = 0;
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '%' && (i + 1) < format.length() &&
                    (format.charAt(i + 1) == 'd' || format.charAt(i + 1) == 'c')) {
                if (!sb.isEmpty()) {
                    StringLiteral str = new StringLiteral(
                            LLVMManager.getInstance().genStringLiteralName(), sb.toString());
                    new PutStrInstr(LLVMManager.getInstance().genLocalVarName(), str);
                    sb.setLength(0);
                }
                Value value = expValue.get(expCnt);
                if (format.charAt(i + 1) == 'd') {
                    if (value.getType().isChar8()) {
                        value = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), value, BaseType.INT32);
                    }
                    new PutIntInstr(LLVMManager.getInstance().genLocalVarName(), value);
                } else if (format.charAt(i + 1) == 'c') {
                    if (value.getType().isChar8()) {
                        value = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), value, BaseType.INT32);
                    }
                    new PutCharInstr(LLVMManager.getInstance().genLocalVarName(), value);
                }
                expCnt++;
                i = i + 1;
            } else if (format.charAt(i) == '\\') {
                if (i + 1 < format.length() && format.charAt(i + 1) == 'n') {
                    sb.append("\n");
                    i = i + 1;
                } else {
                    sb.append("\\");
                }
            } else {
                sb.append(format.charAt(i));
            }
        }
        if (!sb.isEmpty()) {
            StringLiteral str = new StringLiteral(
                    LLVMManager.getInstance().genStringLiteralName(), sb.toString());
            new PutStrInstr(LLVMManager.getInstance().genLocalVarName(), str);
            sb.setLength(0);
        }
        return null;
    }
}
