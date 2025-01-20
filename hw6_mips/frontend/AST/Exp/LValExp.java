package frontend.AST.Exp;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.Token;
import frontend.symbol.*;
import llvm_ir.*;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;
import utils.Printer;
import llvm_ir.instr.GEPInstr;
import llvm_ir.instr.LoadInstr;

import java.util.ArrayList;

public class LValExp extends Exp {
    //LVal → Ident ['[' Exp ']']
    private TokenNode ident;

    public LValExp(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        ident = (TokenNode) children.get(0);
    }

    public ValueType getValueType() {
        if (children.size() == 1) {
            return ident.getValueType();
        } else {
            ValueType vt = ident.getValueType();
            if (vt == ValueType.INT_ARRAY) {
                return ValueType.INT;
            } else if (vt == ValueType.CHAR_ARRAY) {
                return ValueType.CHAR;
            }
        }
        return null;
    }

    public boolean isConst() {
        Symbol symbol = SymbolStack.getInstance().lookupSymbol(ident.getToken().getValue());
        return symbol instanceof ConstSymbol;
    }

    public TokenNode getIdent() {
        return ident;
    }

    @Override
    public void checkError() {
        //先处理本层的error
        Token id = ident.getToken();
        if (SymbolStack.getInstance().lookupSymbol(id.getValue()) == null) {
            Printer.addError("c", id.getLine());
        }
        //再处理子节点的error
        super.checkError();
    }

    public int execute() {
        Symbol symbol = SymbolStack.getInstance().lookupSymbol(ident.getToken().getValue());
        if (symbol instanceof ConstSymbol constSymbol) {
            return constSymbol.getInitial().getValues().get(0);
        }
        return 0;
    }

    //LVal → Ident ['[' Exp ']']
    public Value genIRForAssign() {
        Value expIR = null;
        if (children.size() > 1) {
            expIR = children.get(2).genIR();
        }
        String symbolName = ((TokenNode) children.get(0)).getToken().getValue();
        VarSymbol symbol = (VarSymbol) SymbolStack.getInstance().lookupSymbol(symbolName);
        if (expIR != null) {
            if (expIR.getType().isChar8()) {//一维数组元素
                expIR = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), expIR, BaseType.INT32);
            }
            if (symbol.isCharArray()) {
                //char数组
                return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                        symbol.getLLVMValue(), expIR, BaseType.CHAR8);
            } else {
                //int数组
                return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                        symbol.getLLVMValue(), expIR, BaseType.INT32);
            }
        } else {
            return symbol.getLLVMValue();
        }
    }

    //LVal → Ident ['[' Exp ']']
    public Value genIR() {
        String symbolName = ((TokenNode) children.get(0)).getToken().getValue();
        Symbol symbol = SymbolStack.getInstance().lookupSymbol(symbolName);
        Instr instr;
        if (symbol instanceof ConstSymbol constSymbol) {
            if (children.size() > 1) {
                if (children.get(2) instanceof Exp) {
                    Value expValue = children.get(2).genIR();
                    if (constSymbol.isCharArray()) {
                        instr = new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                constSymbol.getLLVMValue(), expValue, BaseType.CHAR8);
                    } else {
                        instr = new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                constSymbol.getLLVMValue(), expValue, BaseType.INT32);
                    }
                    return new LoadInstr(LLVMManager.getInstance().genLocalVarName(), instr);
                } else {
                    if (constSymbol.getValueType() == ValueType.CHAR_ARRAY) {
                        return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                constSymbol.getLLVMValue(), new Constant(0), BaseType.CHAR8);
                    } else {
                        return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                constSymbol.getLLVMValue(), new Constant(0), BaseType.INT32);
                    }
                }
            } else if (children.size() == 1 && constSymbol.isArray()) {
                if (constSymbol.getValueType() == ValueType.CHAR_ARRAY) {
                    return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                            constSymbol.getLLVMValue(), new Constant(0), BaseType.CHAR8);
                } else {
                    return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                            constSymbol.getLLVMValue(), new Constant(0), BaseType.INT32);
                }
            } else {
                //变量
                return new LoadInstr(LLVMManager.getInstance().genLocalVarName(), constSymbol.getLLVMValue());
            }
        } else {
            //不是const
            VarSymbol varSymbol = (VarSymbol) SymbolStack.getInstance().lookupSymbol(symbolName);
            if (children.size() > 1) {
                if (children.get(2) instanceof Exp) {
                    Value expValue = children.get(2).genIR();
                    if (expValue.getType().isChar8()) {
                        expValue = new ZextInstr(LLVMManager.getInstance().genLocalVarName(), expValue, BaseType.INT32);
                    }
                    if (varSymbol.isCharArray()) {
                        instr = new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                varSymbol.getLLVMValue(), expValue, BaseType.CHAR8);
                    } else {
                        instr = new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                varSymbol.getLLVMValue(), expValue, BaseType.INT32);
                    }
                    return new LoadInstr(LLVMManager.getInstance().genLocalVarName(), instr);
                } else {
                    if (varSymbol.getValueType() == ValueType.CHAR_ARRAY) {
                        return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                varSymbol.getLLVMValue(), new Constant(0), BaseType.CHAR8);
                    } else {
                        return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                                varSymbol.getLLVMValue(), new Constant(0), BaseType.INT32);
                    }
                }

            } else if (children.size() == 1 && varSymbol.isArray()) {
                if (varSymbol.getValueType() == ValueType.CHAR_ARRAY) {
                    return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                            varSymbol.getLLVMValue(), new Constant(0), BaseType.CHAR8);
                } else {
                    return new GEPInstr(LLVMManager.getInstance().genLocalVarName(),
                            varSymbol.getLLVMValue(), new Constant(0), BaseType.INT32);
                }
            } else {
                //变量
                return new LoadInstr(LLVMManager.getInstance().genLocalVarName(), varSymbol.getLLVMValue());
            }
        }
    }//TODO：为啥需要分const和非const？？需要注意
}
