package frontend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Token {
    private TokenType type;
    private String value;
    private int line;

    public Token(String type, String value, int line) {
        this.type = reserveToTokenType(type);
        this.value = value;
        this.line = line;
        printToken();
    }

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
        printToken();
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


    public void setType(TokenType type) {
        this.type = type;
    }

    public void printToken() {
        String outputPath = "lexer.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            writer.write(type.toString() + " " + value + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(type.toString() + " " + value);
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
        }
        return null;
    }
}
