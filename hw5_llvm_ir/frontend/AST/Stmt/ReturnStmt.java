package frontend.AST.Stmt;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.FuncSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import llvm_ir.instr.TruncInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;
import utils.Printer;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.instr.ReturnInstr;

import java.util.ArrayList;

public class ReturnStmt extends Stmt {
    // 'return' [Exp] ';'
    private FuncSymbol parentFunc;

    public ReturnStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        super.checkError();
        FuncSymbol funcSymbol = SymbolStack.getInstance().getLastFunc();
        this.parentFunc = funcSymbol;
        if (funcSymbol != null) {
            ValueType returnType = funcSymbol.getReturnType();
            if (returnType == ValueType.VOID) {
                if (children.size() >= 2 && children.get(1) instanceof Exp) {
                    int errorLine = ((TokenNode) children.get(0)).getToken().getLine();
                    Printer.addError("f", errorLine);
                }
            }
        }
    }

    // 'return' [Exp] ';'
    public Value genIR() {
        Value retValue;
        if (children.get(1) instanceof Exp) {
            retValue = children.get(1).genIR();
            if (retValue.getType() == BaseType.CHAR8 && parentFunc.getReturnType() == ValueType.INT) {
                retValue = new ZextInstr(IRBuilder.getInstance().genLocalVarName(), retValue, BaseType.INT32);
            } else if (retValue.getType() == BaseType.INT32 && parentFunc.getReturnType() == ValueType.CHAR) {
                retValue = new TruncInstr(IRBuilder.getInstance().genLocalVarName(),
                        BaseType.INT32, BaseType.CHAR8, retValue);
            }
        } else {
            retValue = null;
        }
        return new ReturnInstr(IRBuilder.getInstance().genLocalVarName(), retValue);
    }
}
