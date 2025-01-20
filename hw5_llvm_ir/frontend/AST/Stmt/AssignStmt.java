package frontend.AST.Stmt;

import frontend.AST.Exp.LValExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import llvm_ir.instr.TruncInstr;
import llvm_ir.instr.ZextInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import utils.Printer;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.instr.StoreInstr;

import java.util.ArrayList;

public class AssignStmt extends Stmt {
    //LVal '=' Exp ';'
    public AssignStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        //h:当LVal为常量时，不能对其进行修改
        LValExp lValExp = (LValExp) children.get(0);
        TokenNode ident = lValExp.getIdent();
        if (lValExp.isConst()) {
            Printer.addError("h", ident.getToken().getLine());
        }
        super.checkError();
    }

    //LVal '=' Exp ';'
    public Value genIR() {
        Value lVal = ((LValExp) children.get(0)).genIRForAssign();
        PointerType destType = (PointerType) lVal.getType();
        Value exp = children.get(2).genIR();
        if (destType.getDestType() == BaseType.CHAR8 && exp.getType() == BaseType.INT32) {
            exp = new TruncInstr(IRBuilder.getInstance().genLocalVarName(), BaseType.INT32, BaseType.CHAR8, exp);
        } else if (destType.getDestType() == BaseType.INT32 && exp.getType() == BaseType.CHAR8) {
            exp = new ZextInstr(IRBuilder.getInstance().genLocalVarName(), exp, BaseType.INT32);
        }
        return new StoreInstr("", exp, lVal);
    }
}
