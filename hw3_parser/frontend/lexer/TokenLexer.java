package frontend.lexer;

import frontend.utils.Printer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TokenLexer {
    private ArrayList<String> source; //源程序字符串
    private String nowLine;
    private int curPos; //当前指针
    private char curChar; //当前字符
    private String curStr; //当前识别的token字符串
    private Token token; //解析后的token
    private ArrayList<String> reserveWords; //保留字表
    private int lineNum; //记录行号
    private TokenList tokenList; //解析后的token列表
    private boolean isSingle; //是否是单个符号

    public TokenLexer() {
        this.source = new ArrayList<>();
        reserveWords = new ArrayList<>(Arrays.asList("main", "const", "int", "char", "break", "continue",
                "if", "else", "for", "return", "void", "getchar", "getint", "printf"));
        tokenList = new TokenList();
    } //此时lexer拿到的应该是去掉注释和空白行后的源程序字符串

    public void readLines() {
        String path = "testfile.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } //此时source拿的是包括注释的整个源文件

    public void paresToken() {
        readLines(); //读入源文件
        lineNum = 1;
        boolean flag = false; //标记是否位于多行注释
        for (String line : source) {
            if (!flag) { //如果不在多行注释中
                if(line.contains("//")){
                    line = removeSingleComment(line);
                }
                if (line.contains("/*")) {
                    flag = !line.contains("*/");
                    if (!flag) { //同行注释就已经结束
                        line = removeDoubleComment(line); //去掉多行注释
                    } else {
                        line = removeOnceComment(line); //去掉单行注释
                    }
                } else { //位于末尾/*
                    if (line.contains("//")) {
                        line = removeSingleComment(line);
                    }
                }
                if (isBlank(line)) {
                    lineNum++;
                    continue;
                }
            } else if (line.contains("*/")) {
                flag = false;
                line = removeEndComment(line); //去掉多行注释结尾
                if (isBlank(line)) {
                    lineNum++;
                    continue;
                }
            } else {
                lineNum++;
                continue;
            }
            nowLine = line.trim();
            curPos = 0;
            curChar = nowLine.charAt(curPos);
            while (curPos < nowLine.length()) {
                curStr = "";
                while (isSpace()) {
                    getNextChar();
                } //跳过空白字符
                if (isLetter() || curChar == '_') {
                    catChar(); //连接
                    getNextChar();
                    while ((isLetter() || isDigit() || curChar == '_') && curPos < nowLine.length()) {
                        catChar();
                        getNextChar();
                    } //此时拿到的可能是identifier或保留字
                    if (isReservedWord()) {
                        token = new Token(curStr, curStr, lineNum);
                        tokenList.addToken(token);
                    } else {
                        token = new Token(TokenType.IDENFR, curStr, lineNum);
                        tokenList.addToken(token);
                    }
                }//解析标识符和保留字
                else if (isDigit()) {
                    catChar();
                    getNextChar();
                    while (isDigit() && curPos < nowLine.length()) {
                        catChar();
                        getNextChar();
                    }
                    token = new Token(TokenType.INTCON, curStr, lineNum);
                    tokenList.addToken(token);
                }//是数字
                else if (curChar == '\'') {
                    catChar(); //‘
                    getNextChar(); //拿到下一个字符
                    while (curChar != '\'' && curPos < nowLine.length()) {
                        catChar();
                        getNextChar();
                    }
                    catChar(); //将‘连接到curStr
                    getNextChar(); //此时是应该梳理的下一个字符
                    if (curChar == '\'') {
                        catChar();
                        getNextChar();
                    }
                    token = new Token(TokenType.CHRCON, curStr, lineNum);
                    tokenList.addToken(token);
                } //是字符常量
                else if (curChar == '\"') {
                    catChar(); //“
                    getNextChar();
                    while (curChar != '\"' && curPos < nowLine.length()) {
                        if (curChar == '\\') {
                            catChar();
                            getNextChar();
                            if (curChar == '\"') {
                                catChar();
                                getNextChar();
                            }
                        } else {
                            catChar();
                            getNextChar();
                        }
                    }
                    catChar(); //将“连接到curStr
                    getNextChar();
                    token = new Token(TokenType.STRCON, curStr, lineNum);
                    tokenList.addToken(token);
                } //是字符串常量
                else {
                    isSingle = false;
                    singleToken(); //解析单个符号
                    if (!isSingle) {
                        doubleToken(); //解析双符号
                    }
                }
            }
            lineNum++;
        }
    }

    public boolean isSpace() {
        return curChar == ' ' || curChar == '\t';
    }

    public void getNextChar() {
        curPos++;
        if (curPos < nowLine.length() && curPos >= 0) {
            //System.out.println(curPos);
            curChar = nowLine.charAt(curPos);
        }
        if (curPos >= nowLine.length()) {
            curChar = '\0';
        }
    }

    public boolean isLetter() {
        return Character.isLetter(curChar);
    }

    public boolean isDigit() {
        return Character.isDigit(curChar);
    }

    public boolean isReservedWord() {
        return reserveWords.contains(curStr);
    }

    public void catChar() {
        curStr += curChar;
    }

    public void singleToken() {
        if (curChar == '(') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.LPARENT, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == ')') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RPARENT, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '{') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.LBRACE, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '}') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RBRACE, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '[') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.LBRACK, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == ']') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RBRACK, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == ',') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.COMMA, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == ';') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.SEMICN, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '+') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.PLUS, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '-') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MINU, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '*') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MULT, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '/') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.DIV, curStr, lineNum);
            tokenList.addToken(token);
        } else if (curChar == '%') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MOD, curStr, lineNum);
            tokenList.addToken(token);
        }
    }

    public void doubleToken() {
        if (curChar == '!') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.NEQ, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                token = new Token(TokenType.NOT, curStr, lineNum);
                tokenList.addToken(token);
            }
        } else if (curChar == '&') {
            catChar();
            getNextChar();
            if (curChar != '&') {
                Printer.addError("a", lineNum);
                token = new Token(TokenType.AND, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                catChar();
                getNextChar();
                token = new Token(TokenType.AND, curStr, lineNum);
                tokenList.addToken(token);
            }
        } else if (curChar == '|') {
            catChar();
            getNextChar();
            if (curChar != '|') {
                Printer.addError("a", lineNum);
                token = new Token(TokenType.OR, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                catChar();
                getNextChar();
                token = new Token(TokenType.OR, curStr, lineNum);
                tokenList.addToken(token);
            }
        } else if (curChar == '<') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.LEQ, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                token = new Token(TokenType.LSS, curStr, lineNum);
                tokenList.addToken(token);
            }
        } else if (curChar == '>') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.GEQ, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                token = new Token(TokenType.GRE, curStr, lineNum);
                tokenList.addToken(token);
            }
        } else if (curChar == '=') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.EQL, curStr, lineNum);
                tokenList.addToken(token);
            } else {
                token = new Token(TokenType.ASSIGN, curStr, lineNum);
                tokenList.addToken(token);
            }
        }
    }

    public String removeDoubleComment(String line) {
        int commentStart = line.indexOf("/*");
        int commentEnd = line.indexOf("*/");
        return line.substring(0, commentStart) + line.substring(commentEnd + 2);
    }

    public boolean isBlank(String line) {
        return line.trim().isEmpty();
    }

    public String removeOnceComment(String line) {
        int commentStart = line.indexOf("/*");
        return line.substring(0, commentStart);
    }

    public String removeSingleComment(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                line = line.substring(0, i);
                break;
            }
        }
        return line;
    }

    public String removeEndComment(String line) {
        int commentEnd = line.indexOf("*/");
        return line.substring(commentEnd + 2);
    }

    public TokenList getTokenList() {
        return tokenList;
    }
}
