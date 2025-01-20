import frontend.AST.Node;
import frontend.lexer.TokenLexer;
import frontend.parser.Parser;
import frontend.utils.Printer;

public class Compiler {
    public static void main(String[] args) {
        //词法分析
        TokenLexer lexer = new TokenLexer();
        lexer.paresToken(); //此时将文件内容解析为Token
        //语法分析
        Parser parser = new Parser(lexer.getTokenList());
        Node node = parser.parseCompUnit(); //此时将Token解析为AST
        //打印信息
        Printer.printError();
        //语义分析
    }
}