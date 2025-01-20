import frontend.TokenLexer;

public class Compiler {
    public static void main(String[] args) {
        TokenLexer lexer = new TokenLexer();
        lexer.paresToken();
    }
}