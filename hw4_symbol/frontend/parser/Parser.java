package frontend.parser;

import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.lexer.Token;
import frontend.lexer.TokenList;
import frontend.lexer.TokenType;
import frontend.utils.Printer;

import javax.swing.plaf.nimbus.NimbusStyle;
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
            children.add(node);
        }
        return NodeCreate.createNode(children, SyntaxType.CompUnit);
    }

    public Node parseMainFunDef() {
        ArrayList<Node> children = new ArrayList<>();
        //把int main()创建好节点
        for (int i = 0; i < 3; i++) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        }
        if (!curToken.getValue().equals(")")) {
            Printer.addError("j", tokens.lastToken().getLine());
        } else {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        }
        children.add(parseBlock());
        return NodeCreate.createNode(children, SyntaxType.MainFuncDef);
    }

    public Node parseConstDecl() {
        ArrayList<Node> children = new ArrayList<>();
        Node node = null;
        //const
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        //Btype
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        //constDef
        children.add(parseConstDef());
        while (curToken.getValue().equals(",")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseConstDef());
        }
        if (!curToken.getValue().equals(";")) {
            Printer.addError("i", tokens.lastToken().getLine());
        } else {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        }
        return NodeCreate.createNode(children, SyntaxType.ConstDecl);
    }

    public Node parseConstDef() {
        //此时的curToken是该部分要分析的第一个
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        if (curToken.getValue().equals("=")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseConstInitVal());
        } else if (curToken.getValue().equals("[")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseConstExp());
            if (curToken.getValue().equals("]")) {
                children.add(NodeCreate.createNode(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
            //=
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseConstInitVal());
        }
        return NodeCreate.createNode(children, SyntaxType.ConstDef);
    }

    public Node parseConstInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getValue().equals("{")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            if (!curToken.getValue().equals("}")) {
                children.add(parseConstExp());
                while (curToken.getValue().equals(",")) {
                    children.add(NodeCreate.createNode(curToken));
                    curToken = tokens.read();
                    children.add(parseConstExp());
                }
            }
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else if (curToken.getType() == TokenType.STRCON) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            children.add(parseConstExp());
        }
        return NodeCreate.createNode(children, SyntaxType.ConstInitVal);
    }

    public Node parseVarDecl() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //Btype
        curToken = tokens.read();
        children.add(parseVarDef());
        while (curToken.getValue().equals(",")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseVarDef());
        }
        if (curToken.getValue().equals(";")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return NodeCreate.createNode(children, SyntaxType.VarDecl);
    }

    public Node parseVarDef() {
        ArrayList<Node> children = new ArrayList<>();
        //Ident
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        if (curToken.getValue().equals("[")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseConstExp());
            if (curToken.getValue().equals("]")) {
                children.add(NodeCreate.createNode(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        if (curToken.getValue().equals("=")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseInitVal());
        }
        return NodeCreate.createNode(children, SyntaxType.VarDef);
    }

    public Node parseInitVal() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getValue().equals("{")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            if (!curToken.getValue().equals("}")) {
                children.add(parseExp());
                while (curToken.getValue().equals(",")) {
                    children.add(NodeCreate.createNode(curToken));
                    curToken = tokens.read();
                    children.add(parseExp());
                }
            }
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else if (curToken.getType() == TokenType.STRCON) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            children.add(parseExp());
        }
        return NodeCreate.createNode(children, SyntaxType.InitVal);
    }

    public Node parseFuncDef() {
        ArrayList<Node> children = new ArrayList<>();
        //FuncType
        children.add(parseFuncType());
        //Ident
        children.add(NodeCreate.createNode(curToken)); //Ident
        curToken = tokens.read();
        //(
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        //[params]
        if (curToken.getValue().equals("int") || curToken.getValue().equals("char")) {
            children.add(parseFuncFParams());
        }
        if (!curToken.getValue().equals(")")) {
            Printer.addError("j", tokens.lastToken().getLine());
        } else {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        }
        children.add(parseBlock());
        return NodeCreate.createNode(children, SyntaxType.FuncDef);
    }

    public Node parseFuncType() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        return NodeCreate.createNode(children, SyntaxType.FuncType);
    }

    public Node parseFuncFParams() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseFuncFParam());
        while (curToken.getValue().equals(",")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseFuncFParam());
        }
        return NodeCreate.createNode(children, SyntaxType.FuncFParams);
    }

    public Node parseFuncFParam() {
        ArrayList<Node> children = new ArrayList<>();
        //BType
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        //Ident
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        if (curToken.getValue().equals("[")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            if (curToken.getValue().equals("]")) {
                children.add(NodeCreate.createNode(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        return NodeCreate.createNode(children, SyntaxType.FuncFParam);
    }

    public Node parseBlock() {
        ArrayList<Node> children = new ArrayList<>();
        //{
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        //{BlockItems}
        while (!curToken.getValue().equals("}")) {
            children.add(parseBlockItem());
        }
        //}
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        return NodeCreate.createNode(children, SyntaxType.Block);
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
        return NodeCreate.createNode(children, SyntaxType.BlockItem);
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
            return NodeCreate.createNode(children, SyntaxType.Stmt);
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
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return NodeCreate.createNode(children, SyntaxType.ExpStmt);
    }

    public Node parseAssignStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        children.add(parseExp());
        if (curToken.getValue().equals(";")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return NodeCreate.createNode(children, SyntaxType.AssignStmt);
    }

    public Node parseGetIntOrCharStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        boolean isChar = (tokens.preRead(1).getType() == TokenType.GETCHARTK);
        for (int i = 0; i < 3; i++) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        }
        if (curToken.getValue().equals(")")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        if (curToken.getValue().equals(";")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine()); //减一
        }
        if (isChar) {
            return NodeCreate.createNode(children, SyntaxType.GetCharStmt);
        } else {
            return NodeCreate.createNode(children, SyntaxType.GetIntStmt);
        }
    }

    public Node parseIfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //if
        curToken = tokens.read();
        children.add(NodeCreate.createNode(curToken)); //(
        curToken = tokens.read();
        children.add(parseCond());
        if (curToken.getValue().equals(")")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        children.add(parseStmt());
        if (curToken.getValue().equals("else")) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseStmt());
        }
        return NodeCreate.createNode(children, SyntaxType.IfStmt);
    }

    public Node parseFForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //for
        curToken = tokens.read();
        children.add(NodeCreate.createNode(curToken)); //(
        curToken = tokens.read();
        if (!curToken.getValue().equals(";")) {
            children.add(parseForStmt());
        }
        children.add(NodeCreate.createNode(curToken)); //;
        curToken = tokens.read();
        if (!curToken.getValue().equals(";")) {
            children.add(parseCond());
        }
        children.add(NodeCreate.createNode(curToken)); //;
        curToken = tokens.read();
        //ForStmt的FIRST集：IDENT
        if (curToken.getType() == TokenType.IDENFR) {
            children.add(parseForStmt());
        }
        if (curToken.getType() == TokenType.RPARENT) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        children.add(parseStmt());
        return NodeCreate.createNode(children, SyntaxType.FForStmt);
    }

    public Node parseForStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLVal());
        children.add(NodeCreate.createNode(curToken)); //=
        curToken = tokens.read();
        children.add(parseExp());
        return NodeCreate.createNode(children, SyntaxType.ForStmt);
    }

    public Node parseBreakOrContinueStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //break
        boolean isBreak = (curToken.getValue().equals("break"));
        curToken = tokens.read();
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        if (isBreak) {
            return NodeCreate.createNode(children, SyntaxType.BreakStmt);
        } else {
            return NodeCreate.createNode(children, SyntaxType.ContinueStmt);
        }
    }

    public Node parseReturnStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //return
        curToken = tokens.read();
        //Exp的FIRST集：Ident,+-!,(,number,character
        if (curToken.getType() == TokenType.IDENFR || curToken.getType() == TokenType.PLUS
                || curToken.getType() == TokenType.MINU || curToken.getType() == TokenType.NOT
                || curToken.getType() == TokenType.LPARENT || curToken.getType() == TokenType.INTCON
                || curToken.getType() == TokenType.CHRCON) {
            children.add(parseExp());
        }
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return NodeCreate.createNode(children, SyntaxType.ReturnStmt);
    }

    public Node parsePrintfStmt() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //printf
        curToken = tokens.read();
        children.add(NodeCreate.createNode(curToken)); //(
        curToken = tokens.read();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read(); //读入const
        while (curToken.getType() == TokenType.COMMA) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseExp());
        }
        if (curToken.getType() == TokenType.RPARENT) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("j", tokens.lastToken().getLine());
        }
        if (curToken.getType() == TokenType.SEMICN) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
        } else {
            Printer.addError("i", tokens.lastToken().getLine());
        }
        return NodeCreate.createNode(children, SyntaxType.PrintfStmt);
    }

    public Node parseLVal() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken)); //Ident
        curToken = tokens.read();
        if (curToken.getType() == TokenType.LBRACK) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseExp());
            if (curToken.getType() == TokenType.RBRACK) {
                children.add(NodeCreate.createNode(curToken));
                curToken = tokens.read();
            } else {
                Printer.addError("k", tokens.lastToken().getLine());
            }
        }
        return NodeCreate.createNode(children, SyntaxType.LVal);
    }


    public Node parseConstExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return NodeCreate.createNode(children, SyntaxType.ConstExp);
    }


    public Node parseExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        return NodeCreate.createNode(children, SyntaxType.Exp);
    }

    public Node parseCond() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLOrExp());
        return NodeCreate.createNode(children, SyntaxType.Cond);
    }

    public Node parsePrimaryExp() {
        ArrayList<Node> children = new ArrayList<>();
        if (curToken.getType() == TokenType.LPARENT) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseExp());
            if (curToken.getType() == TokenType.RPARENT) {
                children.add(NodeCreate.createNode(curToken));
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
        return NodeCreate.createNode(children, SyntaxType.PrimaryExp);
    }

    public Node parseNumber() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        return NodeCreate.createNode(children, SyntaxType.Number);
    }

    public Node parseChar() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        return NodeCreate.createNode(children, SyntaxType.Character);
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
                children.add(NodeCreate.createNode(curToken));
                curToken = tokens.read(); //Ident
                children.add(NodeCreate.createNode(curToken)); //(
                curToken = tokens.read();
                //FuncRParams的FIRST集：ident,+-!,(,number,character
                if (curToken.getType() == TokenType.IDENFR || curToken.getType() == TokenType.PLUS
                        || curToken.getType() == TokenType.MINU || curToken.getType() == TokenType.NOT
                        || curToken.getType() == TokenType.LPARENT || curToken.getType() == TokenType.INTCON
                        || curToken.getType() == TokenType.CHRCON) {
                    children.add(parseFuncRParams());
                }
                if (curToken.getType() == TokenType.RPARENT) {
                    children.add(NodeCreate.createNode(curToken));
                    curToken = tokens.read();
                } else {
                    Printer.addError("j", tokens.lastToken().getLine());
                }
            } else {
                children.add(parsePrimaryExp());
            }
        }
        return NodeCreate.createNode(children, SyntaxType.UnaryExp);
    }

    public Node parseUnaryOp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(NodeCreate.createNode(curToken));
        curToken = tokens.read();
        return NodeCreate.createNode(children, SyntaxType.UnaryOp);
    }

    public Node parseFuncRParams() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseExp());
        while (curToken.getType() == TokenType.COMMA) {
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseExp());
        }
        return NodeCreate.createNode(children, SyntaxType.FuncRParams);
    }

    public Node parseMulExp() {
        //改写文法
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseUnaryExp());
        while (curToken.getType() == TokenType.MULT || curToken.getType() == TokenType.DIV
                || curToken.getType() == TokenType.MOD) {
            //Printer.printSyntaxType(SyntaxType.MulExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseUnaryExp());
        }
        return NodeCreate.createNode(children, SyntaxType.MulExp);
    }

    public Node parseAddExp() {
        //改写文法
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseMulExp());
        while (curToken.getType() == TokenType.PLUS || curToken.getType() == TokenType.MINU) {
            //Printer.printSyntaxType(SyntaxType.AddExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseMulExp());
        }
        return NodeCreate.createNode(children, SyntaxType.AddExp);
    }

    public Node parseRelExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseAddExp());
        while (curToken.getType() == TokenType.LSS || curToken.getType() == TokenType.GRE
                || curToken.getType() == TokenType.LEQ || curToken.getType() == TokenType.GEQ) {
            //Printer.printSyntaxType(SyntaxType.RelExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseAddExp());
        }
        return NodeCreate.createNode(children, SyntaxType.RelExp);
    }

    public Node parseEqExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseRelExp());
        while (curToken.getType() == TokenType.EQL || curToken.getType() == TokenType.NEQ) {
            //Printer.printSyntaxType(SyntaxType.EqExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseRelExp());
        }
        return NodeCreate.createNode(children, SyntaxType.EqExp);
    }

    public Node parseLAndExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseEqExp());
        while (curToken.getType() == TokenType.AND) {
            //Printer.printSyntaxType(SyntaxType.LAndExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseEqExp());
        }
        return NodeCreate.createNode(children, SyntaxType.LAndExp);
    }

    public Node parseLOrExp() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(parseLAndExp());
        while (curToken.getType() == TokenType.OR) {
            //Printer.printSyntaxType(SyntaxType.LOrExp);
            children.add(NodeCreate.createNode(curToken));
            curToken = tokens.read();
            children.add(parseLAndExp());
        }
        return NodeCreate.createNode(children, SyntaxType.LOrExp);
    }
}
