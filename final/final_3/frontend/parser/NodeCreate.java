package frontend.parser;

import frontend.AST.*;
import frontend.AST.Exp.Character;
import frontend.AST.Exp.Number;
import frontend.AST.Exp.*;
import frontend.AST.Func.*;
import frontend.AST.Stmt.*;
import frontend.AST.Var.*;
import frontend.lexer.Token;

import java.util.ArrayList;

public class NodeCreate {
    public static Node createNode(ArrayList<Node> children, SyntaxType type) {
        switch (type) {
            case CompUnit:
                return new CompUnit(children, type);
            case Block:
                return new Block(children, type);

            case FuncDef:
                return new FuncDef(children, type);
            case MainFuncDef:
                return new MainFuncDef(children, type);
            case FuncFParams:
                return new FuncFParams(children, type);
            case FuncFParam:
                return new FuncFParam(children, type);
            case FuncRParams:
                return new FuncRParams(children, type);
            case FuncType:
                return new FuncType(children, type);

            case ConstDecl:
                return new ConstDecl(children, type);
            case VarDecl:
                return new VarDecl(children, type);
            case ConstDef:
                return new ConstDef(children, type);
            case VarDef:
                return new VarDef(children, type);
            case ConstInitVal:
                return new ConstInitVal(children, type);
            case InitVal:
                return new InitVal(children, type);

            case Exp:
                return new Exp(children, type);
            case MulExp:
                return new MulExp(children, type);
            case AddExp:
                return new AddExp(children, type);
            case RelExp:
                return new RelExp(children, type);
            case EqExp:
                return new EqExp(children, type);
            case LAndExp:
                return new LAndExp(children, type);
            case LOrExp:
                return new LOrExp(children, type);
            case UnaryExp:
                return new UnaryExp(children, type);
            case PrimaryExp:
                return new PrimaryExp(children, type);
            case Number:
                return new Number(children, type);
            case Character:
                return new Character(children, type);
            case UnaryOp:
                return new UnaryOp(children, type);
            case ConstExp:
                return new ConstExp(children, type);
            case Cond:
                return new Cond(children, type);
            case LVal:
                return new LValExp(children, type);

            case Stmt:
                return new Stmt(children, type);
            case ForStmt:
                return new ForStmt(children, type);
            case FForStmt:
                return new FForStmt(children, type);
            case IfStmt:
                return new IfStmt(children, type);
            case AssignStmt:
                return new AssignStmt(children, type);
            case ExpStmt:
                return new ExpStmt(children, type);
            case ReturnStmt:
                return new ReturnStmt(children, type);
            case PrintfStmt:
                return new PrintfStmt(children, type);
            case BreakStmt:
                return new BreakStmt(children, type);
            case ContinueStmt:
                return new ContinueStmt(children, type);
            case BlockItem:
                return new BlockItem(children, type);
            case GetIntStmt:
                return new GetIntStmt(children, type);
            case GetCharStmt:
                return new GetCharStmt(children, type);
            default:
                return null;
        }
    }

    public static Node createNode(Token token) {
        return new TokenNode(token);
    }
}
