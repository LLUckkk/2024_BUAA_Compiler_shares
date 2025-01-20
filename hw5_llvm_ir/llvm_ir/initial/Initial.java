package llvm_ir.initial;

import llvm_ir.Value;
import llvm_ir.type.LLVMType;

import java.util.ArrayList;

public class Initial {
    //initial类的含义
    //initial处理的是所有编译器可以直接计算的表达式
    //尤其是全局变量，其def使得constInitial会new一个initial后加入globalVal
    private LLVMType llvmType;
    private ArrayList<Integer> Values;
    private boolean isChar;
    private String str;


    public Initial(LLVMType llvmType, ArrayList<Integer> values, boolean isChar) {
        this.llvmType = llvmType;
        this.Values = values;
        this.isChar = isChar;
        str = null;
    }

    public Initial(LLVMType llvmType, String str) {
        this.llvmType = llvmType;
        this.str = str;
    }

    public Initial(LLVMType llvmType) {
        this.llvmType = llvmType;
        this.Values = null;
        this.str = null;
    }

    public LLVMType getLLVMType() {
        return llvmType;
    }

    public ArrayList<Integer> getValues() {
        return Values;
    }

    public boolean isChar() {
        return isChar;
    }

    public boolean isString() {
        return str != null;
    }

    public String toString() {
        //用于全局变量的显示
        if (Values != null) {
            if (llvmType.isArray()) {
                if (str == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(llvmType);
                    sb.append(" [");
                    for (int i = 0; i < Values.size(); i++) {
                        Integer v = Values.get(i);
                        if (isChar) {
                            sb.append("i8 ");
                            sb.append(v.toString());
                        } else {
                            sb.append("i32 ");
                            sb.append(v.toString());
                        }
                        if (i != Values.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    return sb.append("]").toString();
                } else {
                    return str;
                }
            } else {
                return llvmType.toString() + " " + Values.get(0);
            }
        } else {
            if (llvmType.isInt32()) {
                return "i32 0";
            } else if (llvmType.isChar8()) {
                return "i8 0";
            } else {
                return llvmType.toString() + " zeroinitializer";
            }
        }
    }
}
