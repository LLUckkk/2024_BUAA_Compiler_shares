package frontend.AST.Stmt;

import frontend.AST.Exp.LValExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import llvm_ir.instr.GetCharInstr;
import llvm_ir.instr.TruncInstr;
import llvm_ir.type.BaseType;
import llvm_ir.type.PointerType;
import utils.Printer;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.StoreInstr;
import llvm_ir.Instr;

import java.util.ArrayList;

public class GetCharStmt extends Stmt {
    public GetCharStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError() {
        LValExp lValExp = (LValExp) children.get(0);
        TokenNode ident = lValExp.getIdent();
        if (lValExp.isConst()) {
            Printer.addError("h", ident.getToken().getLine());
        }
        super.checkError();
    }

    public Value genIR() {
        Instr instr;
        Value pointer = ((LValExp) children.get(0)).genIRForAssign();
        GetCharInstr value = new GetCharInstr(LLVMManager.getInstance().genLocalVarName());
        PointerType pointerType = (PointerType) pointer.getType();
        if (pointerType.getDestType().isChar8()) {
            instr = new TruncInstr(LLVMManager.getInstance().genLocalVarName(), BaseType.INT32, BaseType.CHAR8, value);
        } else {
            instr = value;
        }
        return new StoreInstr("", instr, pointer);
    }
}
