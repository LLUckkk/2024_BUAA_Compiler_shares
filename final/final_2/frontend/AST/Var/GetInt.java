package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import llvm_ir.LLVMManager;
import llvm_ir.Value;
import llvm_ir.instr.GetIntInstr;

import java.util.ArrayList;

public class GetInt extends Node {
    //getint();
    public GetInt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public Value genIR() {
        return new GetIntInstr(LLVMManager.getInstance().genLocalVarName());
    }
}
