package frontend.AST.Stmt;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.utils.Printer;

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
}
