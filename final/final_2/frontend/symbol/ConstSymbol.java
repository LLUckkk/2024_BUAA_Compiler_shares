package frontend.symbol;

import llvm_ir.Value;
import llvm_ir.initial.Initial;

public class ConstSymbol extends Symbol {
    private ValueType valueType;
    private String symbolName;
    private SymbolType symbolType;
    private Initial initial;
    private Value llvmConst;
    private boolean isGlobal;

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
        this.initial = null;
        this.llvmConst = null;
        this.isGlobal = SymbolStack.getInstance().isGlobal();
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setLLVMValue(Value llvmConst) {
        this.llvmConst = llvmConst;
    }

    public Value getLLVMValue() {
        return llvmConst;
    }

    public Initial getInitial() {
        return initial;
    }

    public void setInitial(Initial initial) {
        this.initial = initial;
    }

    public boolean isArray() {
        return valueType == ValueType.CHAR_ARRAY || valueType == ValueType.INT_ARRAY;
    }

    public boolean isCharArray() {
        return valueType == ValueType.CHAR_ARRAY;
    }
}
