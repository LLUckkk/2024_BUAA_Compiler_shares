package frontend.AST.Exp;

import frontend.AST.Func.FuncRParams;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import llvm_ir.*;
import llvm_ir.instr.*;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import utils.Printer;

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

    //文法：PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    public int execute() {
        //first集
        int res = 0;
        if (children.get(0) instanceof UnaryOp) {
            TokenNode op = (TokenNode) children.get(0).getChildren().get(0);
            if (op.getToken().getType() == TokenType.PLUS) {
                res = children.get(1).execute();
            } else if (op.getToken().getType() == TokenType.MINU) {
                res = -children.get(1).execute();
            } else {
                int res_tmp = children.get(1).execute();
                res = 1 - res_tmp;
            }
        } else if (children.get(0) instanceof PrimaryExp) {
            res = children.get(0).execute();
        }
        return res;
    }

    //文法：PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    public Value genIR() {
        if (children.get(0) instanceof PrimaryExp) {
            return children.get(0).genIR();
        } else if (children.get(0) instanceof UnaryOp) {
            //UnaryOp UnaryExp
            Value operand1 = new Constant(0); //为做单目运算准备的常数
            Value operand2 = children.get(1).genIR();
            TokenNode unaryOp = (TokenNode) children.get(0).getChildren().get(0);
            if (unaryOp.getToken().getType() == TokenType.PLUS) {
                return operand2;
            } else if (unaryOp.getToken().getType() == TokenType.MINU) {
                return new CalcInstr(LLVMManager.getInstance().genLocalVarName(),
                        CalcInstr.Op.SUB, operand1, operand2);
            } else {
                if (!(operand2.getType().isInt32())) {
                    operand2 = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), operand2, BaseType.INT32);
                }
                Instr instr = new IcmpInstr(LLVMManager.getInstance().genLocalVarName(),
                        IcmpInstr.IcmpCond.EQ, operand1, operand2); //注意得到的结果是1位，需要扩充到32位
                return new ZextInstr(LLVMManager.getInstance().genLocalVarName(), instr, BaseType.INT32);
            }
        } else { //函数调用
            TokenNode ident = (TokenNode) children.get(0);
            FuncSymbol funcSymbol = (FuncSymbol) SymbolStack.getInstance().lookupSymbol(ident.getToken().getValue());
            ArrayList<ValueType> FParamsType = funcSymbol.getFParamsType();
            Function func = funcSymbol.getLlvmFunc();
            ArrayList<Value> params = new ArrayList<>();
            // FuncRParams → Exp { ',' Exp }
            if (children.size() >= 3 && children.get(2) instanceof FuncRParams) {
                ArrayList<Node> children1 = children.get(2).getChildren();
                for (Node child : children1) {
                    if (child instanceof Exp) {
                        params.add(child.genIR());
                    }
                }
                for (int i = 0; i < FParamsType.size(); i++) {
                    ValueType FParamType = FParamsType.get(i);
                    LLVMType RParamType = params.get(i).getType();
                    if (RParamType == BaseType.INT32 && FParamType == ValueType.CHAR) {
                        Value newParam = new TruncInstr(LLVMManager.getInstance().genLocalVarName(),
                                BaseType.INT32, BaseType.CHAR8, params.get(i));
                        params.set(i, newParam);
                    } else if (RParamType == BaseType.CHAR8 && FParamType == ValueType.INT) {
                        Value newParam = new ZextInstr(LLVMManager.getInstance().genLocalVarName(),
                                params.get(i), BaseType.INT32);
                        params.set(i, newParam);
                    }
                }
            }
            return new CallInstr(LLVMManager.getInstance().genLocalVarName(), func, params);
        }
    }
}
