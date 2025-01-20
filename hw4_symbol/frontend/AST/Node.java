package frontend.AST;

import frontend.lexer.Token;
import frontend.utils.Printer;
import llvm_ir.Value;

import java.util.ArrayList;

public class Node {
    protected ArrayList<Node> children;
    protected SyntaxType type;
    protected Token token;

    public Node(ArrayList<Node> children, SyntaxType type) {
        this.children = children;
        this.type = type;
        this.token = null;
    }

    public Node(Token token) {
        children = null;
        type = SyntaxType.Token;
        this.token = token;
    }

    public void checkError() {
        if (children == null) {
            return;
        } else {
            for (Node child : children) {
                child.checkError();
            }
        }
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }
}
