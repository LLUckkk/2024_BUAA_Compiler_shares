package frontend.AST.Stmt;

import frontend.AST.Exp.LValExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.utils.Printer;

import java.util.ArrayList;

public class GetIntStmt extends Stmt {
    public GetIntStmt(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public void checkError(){
        LValExp lValExp = (LValExp) children.get(0);
        TokenNode ident = lValExp.getIdent();
        if (lValExp.isConst()) {
            Printer.addError("h", ident.getToken().getLine());
        }
        super.checkError();
    }
}
