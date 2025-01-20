package frontend.AST;

import frontend.lexer.Token;
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

    public Value genIR(){
        if(children == null){
            return null; //TokenNode
        } else {
            for(Node child : children){
                child.genIR();
            }
        }
        return null;
    }

    public int execute(){
        return 0;
    } //也就是所有没有定义自己的execute的方法的默认返回0
}
