package frontend.AST.Var;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.lexer.TokenType;

import java.util.ArrayList;

public class ConstDecl extends Node {
    // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    public ConstDecl(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        setConstDefBtype();
    }

    public void setConstDefBtype() {
        TokenNode btype = (TokenNode) children.get(1); //注意是get1
        if (btype.getToken().getType() == TokenType.CHARTK) {
            for (Node child : children) {
                if (child instanceof ConstDef constDef) {
                    constDef.setIsChar();
                }
            }
        }
    }
}
