package llvm_ir.type;

public class BaseType extends LLVMType{
    private int width;
    public static BaseType VOID = new BaseType(0);
    public static BaseType INT32 = new BaseType(32);
    public static BaseType CHAR8 = new BaseType(8);
    public static BaseType INT1 = new BaseType(1);

    private BaseType(int width){
        this.width = width;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(width == 0){
            sb.append("void");
        } else if(width == 32){
            sb.append("i32");
        } else if (width == 8){
            sb.append("i8");
        } else {
            sb.append("i1");
        }
        return sb.toString();
    }
}
