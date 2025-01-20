package frontend.AST.Stmt;

import frontend.AST.Exp.LValExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import llvm_ir.instr.TruncInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.PointerType;
import utils.Printer;
import llvm_ir.IRBuilder;
import llvm_ir.Value;
import llvm_ir.instr.GetIntInstr;
import llvm_ir.instr.StoreInstr;

import java.util.ArrayList;

public class GetIntStmt extends Stmt {
    public GetIntStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    // LVal '=' 'getint''('')'';'
    public void checkError() {
        LValExp lValExp = (LValExp) children.get(0);
        TokenNode ident = lValExp.getIdent();
        if (lValExp.isConst()) {
            Printer.addError("h", ident.getToken().getLine());
        }
        super.checkError();
    }

    public Value genIR() {
        Value pointer = ((LValExp) children.get(0)).genIRForAssign();
        Value value = new GetIntInstr(IRBuilder.getInstance().genLocalVarName());
        PointerType pointerType = (PointerType) pointer.getType();
        if (pointerType.getDestType().isChar8()) {
            value = new TruncInstr(IRBuilder.getInstance().genLocalVarName(), BaseType.INT32, BaseType.CHAR8, value);
        }
        return new StoreInstr("", value, pointer);
    }
}
