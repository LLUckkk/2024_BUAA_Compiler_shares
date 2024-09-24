package frontend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private int num; // 解析数值
    private TokenList tokenList; //解析后的token列表
    private boolean isSingle; //是否是单个符号

    public TokenLexer(ArrayList<String> source) {
        this.source = source;
        reserveWords = new ArrayList<>(Arrays.asList("main", "const", "int", "char", "break", "continue",
                "if", "else", "for", "return", "void", "getchar", "getint", "printf"));
        tokenList = new TokenList();
    } //此时lexer拿到的应该是去掉注释和空白行后的源程序字符串

    public void paresToken() {
        lineNum = 1;
        for (String line : source) {
            nowLine = line.trim();
            curPos = 0;
            curChar = nowLine.charAt(curPos);
            while (curPos < nowLine.length()) {
                //System.out.println("here!!!");
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
                    } else {
                        token = new Token(TokenType.IDENFR, curStr, lineNum);
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
                    if(curChar == '\''){
                        catChar();
                        getNextChar();
                    }
                    token = new Token(TokenType.CHRCON, curStr, lineNum);
                } //是字符常量
                else if (curChar == '\"') {
                    catChar(); //“
                    getNextChar();
                    while (curChar != '\"' && curPos < nowLine.length()) {
                        if(curChar == '\\'){
                            catChar();
                            getNextChar();
                            if(curChar == '\"'){
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
        } else if (curChar == ')') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RPARENT, curStr, lineNum);
        } else if (curChar == '{') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.LBRACE, curStr, lineNum);
        } else if (curChar == '}') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RBRACE, curStr, lineNum);
        } else if (curChar == '[') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.LBRACK, curStr, lineNum);
        } else if (curChar == ']') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.RBRACK, curStr, lineNum);
        } else if (curChar == ',') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.COMMA, curStr, lineNum);
        } else if (curChar == ';') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.SEMICN, curStr, lineNum);
        } else if (curChar == '+') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.PLUS, curStr, lineNum);
        } else if (curChar == '-') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MINU, curStr, lineNum);
        } else if (curChar == '*') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MULT, curStr, lineNum);
        } else if (curChar == '/') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.DIV, curStr, lineNum);
        } else if (curChar == '%') {
            isSingle = true;
            catChar();
            getNextChar();
            token = new Token(TokenType.MOD, curStr, lineNum);
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
            } else {
                token = new Token(TokenType.NOT, curStr, lineNum);
            }
        } else if (curChar == '&') {
            catChar();
            getNextChar();
            if (curChar != '&') {
                printError('a', lineNum);
            } else {
                catChar();
                getNextChar();
                token = new Token(TokenType.AND, curStr, lineNum);
            }
        } else if (curChar == '|') {
            catChar();
            getNextChar();
            if (curChar != '|') {
                printError('a', lineNum);
            } else {
                catChar();
                getNextChar();
                token = new Token(TokenType.OR, curStr, lineNum);
            }
        } else if (curChar == '<') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.LEQ, curStr, lineNum);
            } else {
                token = new Token(TokenType.LSS, curStr, lineNum);
            }
        } else if (curChar == '>') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.GEQ, curStr, lineNum);
            } else {
                token = new Token(TokenType.GRE, curStr, lineNum);
            }
        } else if (curChar == '=') {
            catChar();
            getNextChar();
            if (curChar == '=') {
                catChar();
                getNextChar();
                token = new Token(TokenType.EQL, curStr, lineNum);
            } else {
                token = new Token(TokenType.ASSIGN, curStr, lineNum);
            }
        }
    }

    public void printError(char error, int lineNum) {
        String outputPath = "error.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            writer.write(lineNum + " " + error + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(lineNum + " " + error);
    }
}
