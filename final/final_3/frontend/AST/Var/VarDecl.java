package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;

import java.util.ArrayList;

public class VarDecl extends Node {
    //文法：VarDecl → BType VarDef { ',' VarDef } ';'
    public VarDecl(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        setVarDefBtype();
    }

    public void setVarDefBtype() {
        TokenNode btype = (TokenNode) children.get(0);
        if (btype.getToken().getType() == TokenType.CHARTK) {
            for (Node child : children) {
                if (child instanceof VarDef varDef) {
                    varDef.setIsChar();
                }
            }
        }
    }
}
