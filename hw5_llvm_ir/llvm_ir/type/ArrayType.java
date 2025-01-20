package llvm_ir.type;

public class ArrayType extends LLVMType {
    private LLVMType elementType;
    private int elementNum;

    public ArrayType(LLVMType elementType, int elementNum) {
        this.elementType = elementType;
        this.elementNum = elementNum;
    }

    public ArrayType(LLVMType elementType) {
        this.elementType = elementType;
        this.elementNum = 0;
    } //用于函数参数

    public int getElementNum() {
        return elementNum;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(elementNum);
        sb.append(" x ");
        sb.append(elementType.toString());
        sb.append("]");
        return sb.toString();
    }
}
