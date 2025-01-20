package frontend.AST;

import frontend.lexer.Token;
import frontend.utils.Printer;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> children;
    private SyntaxType type;

    public Node(ArrayList<Node> children, SyntaxType type) {
        this.children = children;
        this.type = type;
        Printer.printSyntaxType(type);
    }

    public Node(Token token) {
        children = null;
        type = SyntaxType.Token;
        Printer.printToken(token);
    }
}
