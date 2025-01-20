package frontend.lexer;

public enum TokenType {
    IDENFR, INTCON, STRCON, CHRCON,
    MAINTK, CONSTTK, INTTK, CHARTK,
    BREAKTK, CONTINUETK, IFTK, ELSETK,
    NOT, AND, OR,
    FORTK, GETINTTK, GETCHARTK, PRINTFTK,
    RETURNTK, PLUS, MINU, VOIDTK,
    MULT, DIV, MOD,
    PLUSS,
    LSS, LEQ, GRE, GEQ, EQL, NEQ,
    ASSIGN, SEMICN, COMMA,
    LPARENT, RPARENT, LBRACK, RBRACK, LBRACE, RBRACE;

    public String toString() {
        return name();
    }
}
