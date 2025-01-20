package frontend.symbol;

import llvm_ir.Value;
import llvm_ir.initial.Initial;
import llvm_ir.type.LLVMType;

public class VarSymbol extends Symbol {
    private String symbolName;
    private ValueType valueType;
    private SymbolType symbolType;
    private Initial initial;
    private boolean isGlobal;
    private Value LLVMValue;
    private LLVMType llvmType;


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
        this.isGlobal = SymbolStack.getInstance().isGlobal();
        this.LLVMValue = null;
        this.initial = null;
        this.llvmType = null;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isArray() {
        return valueType == ValueType.CHAR_ARRAY || valueType == ValueType.INT_ARRAY;
    }

    public boolean isCharArray() {
        return valueType == ValueType.CHAR_ARRAY;
    }

    public boolean isChar() {
        return valueType == ValueType.CHAR;
    }

    public void setLLVMVar(Value LLVMValue) {
        this.LLVMValue = LLVMValue;
    }

    public void setInitial(Initial initial) {
        this.initial = initial;
    }

    public Initial getInitial() {
        return initial;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public Value getLLVMValue() {
        return LLVMValue;
    }

    public void setLLVMType(LLVMType llvmType) {
        this.llvmType = llvmType;
    }

    public LLVMType getLLVMType() {
        return llvmType;
    }
}
