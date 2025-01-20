package frontend.symbol;

import frontend.AST.Var.VarDecl;

public class VarSymbol extends Symbol {
    private String symbolName;
    private ValueType valueType;
    private SymbolType symbolType;

    public VarSymbol(String symbolName, ValueType valueType) {
        super(symbolName);
        this.symbolName = symbolName;
        this.valueType = valueType;
        if (valueType == ValueType.CHAR) {
            symbolType = SymbolType.Char;
        } else if (valueType == ValueType.INT) {
            symbolType = SymbolType.Int;
        } else if (valueType == ValueType.INT_ARRAY) {
            symbolType = SymbolType.IntArray;
        } else if (valueType == ValueType.CHAR_ARRAY) {
            symbolType = SymbolType.CharArray;
        }
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public ValueType getValueType() {
        return valueType;
    }
}
