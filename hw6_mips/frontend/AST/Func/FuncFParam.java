package frontend.AST.Func;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.symbol.VarSymbol;
import utils.Printer;
import llvm_ir.LLVMManager;
import llvm_ir.Instr;
import llvm_ir.Param;
import llvm_ir.Value;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

public class FuncFParam extends Node {
    //文法：FuncFParam → BType Ident ['[' ']']
    private VarSymbol varSymbol;

    public FuncFParam(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public VarSymbol createVarSymbol() {
        TokenNode bType = (TokenNode) children.get(0);
        TokenNode ident = (TokenNode) children.get(1);
        String symbolName = ident.getToken().getValue();
        TokenType bTypeToken = bType.getToken().getType();
        ValueType valueType;
        if (children.size() == 2) {
            if (bTypeToken == TokenType.INTTK) {
                valueType = ValueType.INT;
            } else {
                valueType = ValueType.CHAR;
            }
        } else {
            if (bTypeToken == TokenType.INTTK) {
                valueType = ValueType.INT_ARRAY;
            } else {
                valueType = ValueType.CHAR_ARRAY;
            }
        }
        return new VarSymbol(symbolName, valueType);
    }

    public void checkError() {
        this.varSymbol = createVarSymbol();
        //b:重复定义
        if (!SymbolStack.getInstance().addSymbol(varSymbol)) {
            int errorLine = ((TokenNode) children.get(1)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
        super.checkError();
    }

    public ValueType getFParamType() {
        if (varSymbol == null) {
            TokenNode bType = (TokenNode) children.get(0);
            TokenType bTypeToken = bType.getToken().getType();
            if (children.size() == 2) {
                if (bTypeToken == TokenType.INTTK) {
                    return ValueType.INT;
                } else {
                    return ValueType.CHAR;
                }
            } else {
                if (bTypeToken == TokenType.INTTK) {
                    return ValueType.INT_ARRAY;
                } else {
                    return ValueType.CHAR_ARRAY;
                }
            }
        } else {
            return varSymbol.getValueType();
        }
    }

    public Value genIR() {
        //FuncFParam → BType Ident ['[' ']']
        SymbolStack.getInstance().addSymbol(varSymbol);
        LLVMType type;
        if (varSymbol.isArray()) {
            if (varSymbol.isCharArray()) {
                type = new PointerType(BaseType.CHAR8);
            } else {
                type = new PointerType(BaseType.INT32);
            }
        } else if (varSymbol.isChar()) {
            type = BaseType.CHAR8;
        } else {
            type = BaseType.INT32;
        }
        Param param = new Param(type,
                LLVMManager.getInstance().genParamName());
        if (param.isBase()) {
            Instr instr = new AllocaInstr(param.getType(), LLVMManager.getInstance().genLocalVarName());
            varSymbol.setLLVMVar(instr);
            new StoreInstr("", param, instr);
        } else {
            varSymbol.setLLVMVar(param);
        }
        super.genIR();
        return null;
    }
}
