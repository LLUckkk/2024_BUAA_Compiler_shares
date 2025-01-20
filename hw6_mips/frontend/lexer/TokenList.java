package frontend.lexer;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Token> tokens; //不确定数据结构，先暂存
    private int curPos;
    private int savedPos;

    public TokenList() {
        tokens = new ArrayList<>();
        curPos = 0;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public Token read() {
        if (curPos == tokens.size()) {
            return null;
        }
        return tokens.get(curPos++);
    }

    public Token preRead(int step) {
        if (curPos + step - 1 >= tokens.size()) {
            return null;
        } else {
            return tokens.get(curPos + step - 1);
        }
    }

    public void savePos() {
        savedPos = curPos - 1;
    }

    public Token backSavedPos() {
        curPos = savedPos;
        return tokens.get(curPos++);
    }

    public Token lastToken() {
        return tokens.get(curPos - 2);
    }
}
