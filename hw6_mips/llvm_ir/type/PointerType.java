package llvm_ir.type;

public class PointerType extends LLVMType {
    private LLVMType destType;

    public PointerType(LLVMType destType) {
        this.destType = destType;
    }

    public LLVMType getDestType() {
        return destType;
    }

    public String toString() {
        return destType.toString() + "*";
    }
}
