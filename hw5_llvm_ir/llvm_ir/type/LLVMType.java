package llvm_ir.type;

public class LLVMType {
    public boolean isArray(){
        return this instanceof ArrayType;
    }

    public boolean isInt32(){
        return this == BaseType.INT32;
    }

    public boolean isChar8(){
        return this == BaseType.CHAR8;
    }
}
