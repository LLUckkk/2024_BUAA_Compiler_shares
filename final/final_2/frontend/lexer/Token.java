package frontend.lexer;

public class Token {
    private TokenType type;
    private String value;
    private int line;

    public Token(String type, String value, int line) {
        this.type = reserveToTokenType(type);
        this.value = value;
        this.line = line;
    }

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public TokenType reserveToTokenType(String type) {
        switch (type) {
            case "main":
                return TokenType.MAINTK;
            case "int":
                return TokenType.INTTK;
            case "const":
                return TokenType.CONSTTK;
            case "char":
                return TokenType.CHARTK;
            case "break":
                return TokenType.BREAKTK;
            case "continue":
                return TokenType.CONTINUETK;
            case "if":
                return TokenType.IFTK;
            case "else":
                return TokenType.ELSETK;
            case "for":
                return TokenType.FORTK;
            case "return":
                return TokenType.RETURNTK;
            case "void":
                return TokenType.VOIDTK;
            case "getint":
                return TokenType.GETINTTK;
            case "getchar":
                return TokenType.GETCHARTK;
            case "printf":
                return TokenType.PRINTFTK;
            case "bitand":
                return TokenType.BITAND;
        }
        return null;
    }
}
