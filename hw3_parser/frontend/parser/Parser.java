package frontend.parser;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.lexer.Token;
import frontend.lexer.TokenList;
import frontend.lexer.TokenType;
import frontend.utils.Printer;

import java.util.ArrayList;


public class Parser {
    private TokenList tokens;
    private Token curToken;

    public Parser(TokenList tokens) {
        this.tokens = tokens;
        curToken = this.tokens.read();
    }

    public Node parseCompUnit() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        while (true) {
            if (curToken == null) {
                break;
            }
            /* root node*/
            if (tokens.preRead(1).getType() == TokenType.MAINTK) {
                node = parseMainFunDef();
            } else if (tokens.preRead(2).getType() == TokenType.LPARENT) {
                node = parseFuncDef();
            } else if (curToken.getType() == TokenType.CONSTTK) {
                node = parseConstDecl();
            } else if (curToken.getType() == TokenType.INTTK || curToken.getType() == TokenType.CHARTK) {
                node = parseVarDecl();
            } else {
                break;
            }
        }
        children.add(node);
        return new Node(children, SyntaxType.CompUnit);
    }

    public Node parseMainFunDef() {
        ArrayList<Node> children = new ArrayList<>();
        //把int main()创建好节点
        for (int i = 0; i < 3; i++) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        }
        if (!curToken.getValue().equals(")")) {
            Printer.addError("j", tokens.lastToken().getLine());
        } else {
            children.add(new Node(curToken));
            curToken = tokens.read();
        }
        Node node = parseBlock();
        children.add(node);
        return new Node(children, SyntaxType.MainFuncDef);
    }

    public Node parseConstDecl() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        //const
        children.add(new Node(curToken));
        curToken = tokens.read();
        //Btype
        children.add(new Node(curToken));
        curToken = tokens.read();
        //constDef
        node = parseConstDef();
        children.add(node);
        while (curToken.getValue().equals(",")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseConstDef();
            children.add(node);
        }
        if (!curToken.getValue().equals(";")) {
            Printer.addError("i", tokens.lastToken().getLine());
        } else {
            children.add(new Node(curToken));
            curToken = tokens.read();
        }
        return new Node(children, SyntaxType.ConstDecl);
    }

    public Node parseConstDef() {
        //此时的curToken是该部分要分析的第一个
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken));
        curToken = tokens.read();
        Node node = null;
        if (curToken.getValue().equals("=")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseConstInitVal();
            children.add(node);
        } else if (curToken.getValue().equals("[")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseConstExp();
            children.add(node);
            if (curToken.getValue().equals("]")) {
                children.add(new Node(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
            //=
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseConstInitVal();
            children.add(node);
        }
        return new Node(children, SyntaxType.ConstDef);
    }

    public Node parseConstInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        if (curToken.getValue().equals("{")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            if (!curToken.getValue().equals("}")) {
                node = parseConstExp();
                children.add(node);
                while (curToken.getValue().equals(",")) {
                    children.add(new Node(curToken));
                    curToken = tokens.read();
                    node = parseConstExp();
                    children.add(node);
                }
            }
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else if (curToken.getType() == TokenType.STRCON) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            node = parseConstExp();
            children.add(node);
        }
        return new Node(children, SyntaxType.ConstInitVal);
    }

    public Node parseVarDecl() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        children.add(new Node(curToken)); //Btype
        curToken = tokens.read();
        node = parseVarDef();
        children.add(node);
        while (curToken.getValue().equals(",")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseVarDef();
            children.add(node);
        }
        if (curToken.getValue().equals(";")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.VarDecl);
    }

    public Node parseVarDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        //Ident
        children.add(new Node(curToken));
        curToken = tokens.read();
        if (curToken.getValue().equals("[")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseConstExp();
            children.add(node);
            if (curToken.getValue().equals("]")) {
                children.add(new Node(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        if (curToken.getValue().equals("=")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseInitVal();
            children.add(node);
        }
        return new Node(children, SyntaxType.VarDef);
    }

    public Node parseInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        if (curToken.getValue().equals("{")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            if (!curToken.getValue().equals("}")) {
                node = parseExp();
                children.add(node);
                while (curToken.getValue().equals(",")) {
                    children.add(new Node(curToken));
                    curToken = tokens.read();
                    node = parseExp();
                    children.add(node);
                }
            }
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else if (curToken.getType() == TokenType.STRCON) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            node = parseExp();
            children.add(node);
        }
        return new Node(children, SyntaxType.InitVal);
    }

    public Node parseFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        //FuncType
        children.add(parseFuncType());
        //Ident
        children.add(new Node(curToken)); //Ident
        curToken = tokens.read();
        //(
        children.add(new Node(curToken));
        curToken = tokens.read();
        //[params]
        if (curToken.getValue().equals("int") || curToken.getValue().equals("char")) {
            node = parseFuncFParams();
            children.add(node);
        }
        if (!curToken.getValue().equals(")")) {
            Printer.addError("j", tokens.lastToken().getLine());
        } else {
            children.add(new Node(curToken));
            curToken = tokens.read();
        }
        children.add(parseBlock());
        return new Node(children, SyntaxType.FuncDef);
    }

    public Node parseFuncType() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken));
        curToken = tokens.read();
        return new Node(children, SyntaxType.FuncType);
    }

    public Node parseFuncFParams() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        node = parseFuncFParam();
        children.add(node);
        while (curToken.getValue().equals(",")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            node = parseFuncFParam();
            children.add(node);
        }
        return new Node(children, SyntaxType.FuncFParams);
    }

    public Node parseFuncFParam() {
        ArrayList<Node> children = new ArrayList<>();
        //BType
        children.add(new Node(curToken));
        curToken = tokens.read();
        //Ident
        children.add(new Node(curToken));
        curToken = tokens.read();
        if (curToken.getValue().equals("[")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            if (curToken.getValue().equals("]")) {
                children.add(new Node(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        return new Node(children, SyntaxType.FuncFParam);
    }

    public Node parseBlock() {
        ArrayList<Node> children = new ArrayList<>();
        //{
        children.add(new Node(curToken));
        curToken = tokens.read();
        //{BlockItems}
        while (!curToken.getValue().equals("}")) {
            children.add(parseBlockItem());
        }
        //}
        children.add(new Node(curToken));
        curToken = tokens.read();
        return new Node(children, SyntaxType.Block);
    }

    public Node parseBlockItem() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getValue().equals("int") ||
                curToken.getValue().equals("char")) {
            children.add(parseVarDecl());
        } else if (curToken.getValue().equals("const")) {
            children.add(parseConstDecl());
        } else {
            children.add(parseStmt());
        }
        return new Node(children, SyntaxType.BlockItem);
    }

    public Node parseStmt() {
        if (curToken.getValue().equals("if")) {
            return parseIfStmt();
        } else if (curToken.getValue().equals("for")) {
            return parseFForStmt();
        } else if (curToken.getValue().equals("break")) {
            return parseBreakOrContinueStmt();
        } else if (curToken.getValue().equals("continue")) {
            return parseBreakOrContinueStmt();
        } else if (curToken.getValue().equals("return")) {
            return parseReturnStmt();
        } else if (curToken.getValue().equals("printf")) {
            return parsePrintfStmt();
        } else if (curToken.getValue().equals("{")) {
            ArrayList<Node> children = new ArrayList<>();
            children.add(parseBlock());
            return new Node(children, SyntaxType.Stmt);
        } else {
            //保存pos，关掉printer的报错
            tokens.savePos();
            Printer.setOff();
            parseExp();
            Printer.setOn();
            //此时如果是LVal那些赋值语句，此时curtOKEN =
            if (curToken.getValue().equals("=")) {
                if (tokens.preRead(1).getType() == TokenType.GETINTTK) {
                    curToken = tokens.backSavedPos();
                    return parseGetIntOrCharStmt();
                } else if (tokens.preRead(1).getType() == TokenType.GETCHARTK) {
                    curToken = tokens.backSavedPos();
                    return parseGetIntOrCharStmt();
                } else {
                    curToken = tokens.backSavedPos();
                    return parseAssignStmt();
                }
            } else {
                curToken = tokens.backSavedPos();
                return parseExpStmt();
            }
        }
    }

    public Node parseExpStmt() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getType() == TokenType.IDENFR || curToken.getType() == TokenType.PLUS
                || curToken.getType() == TokenType.MINU || curToken.getType() == TokenType.NOT
                || curToken.getType() == TokenType.LPARENT || curToken.getType() == TokenType.INTCON
                || curToken.getType() == TokenType.CHRCON) {
            children.add(parseExp());
        }
        if (curToken.getValue().equals(";")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseAssignStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        children.add(new Node(curToken));
        curToken = tokens.read();
        children.add(parseExp());
        if (curToken.getValue().equals(";")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseGetIntOrCharStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        for (int i = 0; i < 3; i++) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        }
        if (curToken.getValue().equals(")")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        if (curToken.getValue().equals(";")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine()); //减一
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseIfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //if
        curToken = tokens.read();
        children.add(new Node(curToken)); //(
        curToken = tokens.read();
        children.add(parseCond());
        if (curToken.getValue().equals(")")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        children.add(parseStmt());
        if (curToken.getValue().equals("else")) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseStmt());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseFForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //for
        curToken = tokens.read();
        children.add(new Node(curToken)); //(
        curToken = tokens.read();
        if (!curToken.getValue().equals(";")) {
            children.add(parseForStmt());
        }
        children.add(new Node(curToken)); //;
        curToken = tokens.read();
        if (!curToken.getValue().equals(";")) {
            children.add(parseCond());
        }
        children.add(new Node(curToken)); //;
        curToken = tokens.read();
        //ForStmt的FIRST集：IDENT
        if (curToken.getType() == TokenType.IDENFR) {
            children.add(parseForStmt());
        }
        if (curToken.getType() == TokenType.RPARENT) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        children.add(parseStmt());
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        children.add(new Node(curToken)); //=
        curToken = tokens.read();
        children.add(parseExp());
        return new Node(children, SyntaxType.ForStmt);
    }

    public Node parseBreakOrContinueStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //break
        curToken = tokens.read();
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseReturnStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //return
        curToken = tokens.read();
        //Exp的FIRST集：Ident,+-!,(,number,character
        if (curToken.getType() == TokenType.IDENFR || curToken.getType() == TokenType.PLUS
                || curToken.getType() == TokenType.MINU || curToken.getType() == TokenType.NOT
                || curToken.getType() == TokenType.LPARENT || curToken.getType() == TokenType.INTCON
                || curToken.getType() == TokenType.CHRCON) {
            children.add(parseExp());
        }
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parsePrintfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //printf
        curToken = tokens.read();
        children.add(new Node(curToken)); //(
        curToken = tokens.read();
        children.add(new Node(curToken));
        curToken = tokens.read(); //读入const
        while (curToken.getType() == TokenType.COMMA) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseExp());
        }
        if (curToken.getType() == TokenType.RPARENT) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(new Node(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return new Node(children, SyntaxType.Stmt);
    }

    public Node parseLVal() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken)); //Ident
        curToken = tokens.read();
        if (curToken.getType() == TokenType.LBRACK) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseExp());
            if (curToken.getType() == TokenType.RBRACK) {
                children.add(new Node(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        return new Node(children, SyntaxType.LVal);
    }


    public Node parseConstExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return new Node(children, SyntaxType.ConstExp);
    }


    public Node parseExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return new Node(children, SyntaxType.Exp);
    }

    public Node parseCond() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLOrExp());
        return new Node(children, SyntaxType.Cond);
    }

    public Node parsePrimaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getType() == TokenType.LPARENT) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseExp());
            if (curToken.getType() == TokenType.RPARENT) {
                children.add(new Node(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("j", tokens.lastToken().getLine());
            }
        } else if (curToken.getType() == TokenType.INTCON) {
            children.add(parseNumber());
        } else if (curToken.getType() == TokenType.IDENFR) {
            children.add(parseLVal());
        } else if (curToken.getType() == TokenType.CHRCON) {
            children.add(parseChar());
        }
        return new Node(children, SyntaxType.PrimaryExp);
    }

    public Node parseNumber() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken));
        curToken = tokens.read();
        return new Node(children, SyntaxType.Number);
    }

    public Node parseChar() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken));
        curToken = tokens.read();
        return new Node(children, SyntaxType.Character);
    }

    public Node parseUnaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getType() == TokenType.PLUS || curToken.getType() == TokenType.MINU
                || curToken.getType() == TokenType.NOT) {
            children.add(parseUnaryOp());
            children.add(parseUnaryExp());
        } else if (curToken.getType() == TokenType.INTCON || curToken.getType() == TokenType.CHRCON) {
            children.add(parsePrimaryExp());
        } else if (curToken.getType() == TokenType.LPARENT) {
            children.add(parsePrimaryExp());
        } else { //preRead
            if (tokens.preRead(1).getType() == TokenType.LPARENT) {
                children.add(new Node(curToken));
                curToken = tokens.read(); //Ident
                children.add(new Node(curToken)); //(
                curToken = tokens.read();
                //FuncRParams的FIRST集：ident,+-!,(,number,character
                if (curToken.getType() == TokenType.IDENFR || curToken.getType() == TokenType.PLUS
                        || curToken.getType() == TokenType.MINU || curToken.getType() == TokenType.NOT
                        || curToken.getType() == TokenType.LPARENT || curToken.getType() == TokenType.INTCON
                        || curToken.getType() == TokenType.CHRCON) {
                    children.add(parseFuncRParams());
                }
                if (curToken.getType() == TokenType.RPARENT) {
                    children.add(new Node(curToken));
                    curToken = tokens.read();
                } else {
                    Printer.addError("j", tokens.lastToken().getLine());
                }
            } else {
                children.add(parsePrimaryExp());
            }
        }
        return new Node(children, SyntaxType.UnaryExp);
    }

    public Node parseUnaryOp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(new Node(curToken));
        curToken = tokens.read();
        return new Node(children, SyntaxType.UnaryOp);
    }

    public Node parseFuncRParams() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseExp());
        while (curToken.getType() == TokenType.COMMA) {
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseExp());
        }
        return new Node(children, SyntaxType.FuncRParams);
    }

    public Node parseMulExp() {
        //改写文法
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseUnaryExp());
        while (curToken.getType() == TokenType.MULT || curToken.getType() == TokenType.DIV
                || curToken.getType() == TokenType.MOD) {
            Printer.printSyntaxType(SyntaxType.MulExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseUnaryExp());
        }
        return new Node(children, SyntaxType.MulExp);
    }

    public Node parseAddExp() {
        //改写文法
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseMulExp());
        while (curToken.getType() == TokenType.PLUS || curToken.getType() == TokenType.MINU) {
            Printer.printSyntaxType(SyntaxType.AddExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseMulExp());
        }
        return new Node(children, SyntaxType.AddExp);
    }

    public Node parseRelExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        while (curToken.getType() == TokenType.LSS || curToken.getType() == TokenType.GRE
                || curToken.getType() == TokenType.LEQ || curToken.getType() == TokenType.GEQ) {
            Printer.printSyntaxType(SyntaxType.RelExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseAddExp());
        }
        return new Node(children, SyntaxType.RelExp);
    }

    public Node parseEqExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseRelExp());
        while (curToken.getType() == TokenType.EQL || curToken.getType() == TokenType.NEQ) {
            Printer.printSyntaxType(SyntaxType.EqExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseRelExp());
        }
        return new Node(children, SyntaxType.EqExp);
    }

    public Node parseLAndExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseEqExp());
        while (curToken.getType() == TokenType.AND) {
            Printer.printSyntaxType(SyntaxType.LAndExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseEqExp());
        }
        return new Node(children, SyntaxType.LAndExp);
    }

    public Node parseLOrExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLAndExp());
        while (curToken.getType() == TokenType.OR) {
            Printer.printSyntaxType(SyntaxType.LOrExp);
            children.add(new Node(curToken));
            curToken = tokens.read();
            children.add(parseLAndExp());
        }
        return new Node(children, SyntaxType.LOrExp);
    }
}
