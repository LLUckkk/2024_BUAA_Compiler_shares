import frontend.Preprocess;
import frontend.TokenLexer;

public class Compiler {
    public static void main(String[] args) {
        Preprocess pre = new Preprocess();
        //pre.getLines();
        TokenLexer lexer = new TokenLexer(pre.getLines());
        lexer.paresToken();
    }
}