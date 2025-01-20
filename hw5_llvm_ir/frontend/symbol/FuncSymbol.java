package frontend.symbol;

import java.util.ArrayList;
import frontend.symbol.SymbolType;
import llvm_ir.Function;

public class FuncSymbol extends Symbol {
    private ValueType returnType;
    private SymbolType symbolType;
    private String symbolName;
    private ArrayList<ValueType> paramsType;
    private Function llvmFunc;

    public FuncSymbol(String symbolName, ValueType returnType) {
        super(symbolName);
        if (returnType.equals(ValueType.VOID)) {
            this.symbolType = SymbolType.VoidFunc;
        } else if (returnType.equals(ValueType.INT)) {
            this.symbolType = SymbolType.IntFunc;
        } else {
            this.symbolType = SymbolType.CharFunc;
        }
        this.returnType = returnType;
        this.symbolName = symbolName;
        this.paramsType = null;
        this.llvmFunc = null;
    }

    public FuncSymbol(String symbolName, ValueType returnType,
                      ArrayList<ValueType> paramsType) {
        super(symbolName);
        this.returnType = returnType;
        this.symbolName = symbolName;
        this.paramsType = paramsType;
        if (returnType.equals(ValueType.VOID)) {
            this.symbolType = SymbolType.VoidFunc;
        } else if (returnType.equals(ValueType.INT)) {
            this.symbolType = SymbolType.IntFunc;
        } else {
            symbolType = SymbolType.CharFunc;
        }
    }

    public ArrayList<ValueType> getFParamsType() {
        return paramsType;
    }

    public int getFParamsNum() {
        if(paramsType == null){
            return 0;
        } else {
            return paramsType.size();
        }
    }

    public ValueType getReturnType() {
        return returnType;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setLlvmFunc(Function llvmFunc) {
        this.llvmFunc = llvmFunc;
    }

    public Function getLlvmFunc() {
        return llvmFunc;
    }
}
