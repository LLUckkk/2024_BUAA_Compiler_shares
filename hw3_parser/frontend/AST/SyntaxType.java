package frontend.AST;

public enum SyntaxType {
    CompUnit, Decl, FuncDef, MainFuncDef, Token, ConstDecl,
    VarDecl, ConstDef, ConstInitVal, ConstExp, VarDef, InitVal,
    FuncFParams, FuncFParam, Block, BlockItem, Stmt, LVal, ForStmt,
    Cond, Exp, PrimaryExp, Number, Character, UnaryOp, FuncRParams,
    MulExp, AddExp, RelExp, EqExp, LAndExp, LOrExp, UnaryExp, FuncType
}
