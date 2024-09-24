package frontend;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Token> tokens; //不确定数据结构，先暂存

    public TokenList() {
        tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

}
