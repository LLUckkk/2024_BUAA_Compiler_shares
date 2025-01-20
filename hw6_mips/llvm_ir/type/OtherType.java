package llvm_ir.type;

public class OtherType extends LLVMType {
    public static OtherType BLOCK = new OtherType();
    public static OtherType FUNCTION = new OtherType();
    public static OtherType MODULE = new OtherType();
    //暂时不知道这些东西是干啥的，先留着
}
