package frontend.symbol;

public class ConstSymbol extends Symbol {
    private ValueType valueType;
    private String symbolName;
    private SymbolType symbolType;

    public ConstSymbol(String symbolName, ValueType valueType) {
        super(symbolName);
        this.valueType = valueType;
        if (valueType == ValueType.INT) {
            this.symbolType = SymbolType.ConstInt;
        } else if (valueType == ValueType.CHAR) {
            this.symbolType = SymbolType.ConstChar;
        } else if (valueType == ValueType.CHAR_ARRAY) {
            this.symbolType = SymbolType.ConstCharArray;
        } else if (valueType == ValueType.INT_ARRAY) {
            this.symbolType = SymbolType.ConstIntArray;
        }
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public ValueType getValueType() {
        return valueType;
    }
}
