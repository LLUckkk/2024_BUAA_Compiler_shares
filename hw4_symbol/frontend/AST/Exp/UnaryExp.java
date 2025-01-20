package frontend.AST.Exp;

import frontend.AST.Func.FuncRParams;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.utils.Printer;

import java.util.ArrayList;

public class UnaryExp extends Exp {
    //文法：PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    public UnaryExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ((PrimaryExp) children.get(0)).getValueType();
        } else if (haveOp()) {
            return ValueType.INT;
        } else {
            TokenNode tokenNode = (TokenNode) children.get(0);
            return tokenNode.getValueType();
        }
    }

    public boolean haveOp() {
        for (Node child : children) {
            if (child instanceof UnaryOp) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkError() {
        if (children.get(0) instanceof TokenNode temp) {
            //c：未定义的标识符
            if (SymbolStack.getInstance().lookupSymbol(temp.getToken().getValue()) == null) {
                Printer.addError("c", temp.getToken().getLine());
                super.checkError();
                return;
            }
            //找到此处调用的函数
            FuncSymbol funcSymbol = (FuncSymbol) SymbolStack.getInstance().lookupSymbol(temp.getToken().getValue());
            if (children.size() >= 3 && children.get(2) instanceof FuncRParams funcRParams) {
                super.checkError();
                funcRParams.setRParams();
                //d:函数参数个数不匹配
                int FParamsNum = funcSymbol.getFParamsNum();
                int RParamsNum = funcRParams.getRParamsNum();
                if (FParamsNum != RParamsNum) {
                    TokenNode ident = (TokenNode) children.get(0);
                    Printer.addError("d", ident.getToken().getLine());
                    super.checkError();
                    return;
                }
                //e:函数参数类型不匹配
                ArrayList<ValueType> FParamsType = funcSymbol.getFParamsType();
                ArrayList<ValueType> RParamsType = funcRParams.getRParamsType();
                for (int i = 0; i < FParamsNum; i++) {
                    if (FParamsType.get(i) == ValueType.CHAR && RParamsType.get(i) == ValueType.INT) {
                        continue;
                    } else if (FParamsType.get(i) == ValueType.INT && RParamsType.get(i) == ValueType.CHAR) {
                        continue;
                    }
                    if (FParamsType.get(i) != RParamsType.get(i)) {
                        TokenNode ident = (TokenNode) children.get(0);
                        Printer.addError("e", ident.getToken().getLine());
                        //System.out.println("type incorrect in function: " + funcSymbol.getSymbolName());
                        break;
                    }
                }
            }
        } else {
            super.checkError();
        }
    }
}
