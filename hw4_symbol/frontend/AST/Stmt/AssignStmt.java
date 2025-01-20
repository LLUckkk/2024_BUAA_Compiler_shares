package frontend.AST.Stmt;

import frontend.AST.Exp.LValExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.utils.Printer;

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
}
