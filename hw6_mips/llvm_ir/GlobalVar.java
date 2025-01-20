package llvm_ir;

import llvm_ir.initial.Initial;
import llvm_ir.type.ArrayType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;
import mips.assembly.GlobalAsmb;
import mips.assembly.GlobalByte;
import mips.assembly.GlobalWord;

public class GlobalVar extends User {
    private Initial init;

    public GlobalVar(LLVMType type, String name, Initial init) {
        super(type, name);
        this.init = init;
        LLVMManager.getInstance().addGlobalVar(this);
    } //是一个指针变量

    public String toString() {
        return name + " = dso_local global " + init.toString();
    }

    public void toMips() {
        //分情况，包括数组变量和int char
        LLVMType type1 = ((PointerType) type).getDestType();
        if (type1.isInt32()) {
            if (init.getValues() != null) {
                new GlobalWord(name.substring(1), init.getValues().get(0));
            } else {
                //没有初始值
                new GlobalWord(name.substring(1), 0);
            }
        } else if (type1.isChar8()) {
            if (init.getValues() != null) {
                new GlobalByte(name.substring(1), init.getValues().get(0));
            } else {
                new GlobalByte(name.substring(1), 0);
            }
        } else if (type1.isArray()) {
            if (init.getValues() != null) {
                if (((ArrayType) type1).isCharArray()) {
                    new GlobalByte(name.substring(1), init.getValues());
                } else {
                    new GlobalWord(name.substring(1), init.getValues());
                }
            } else {
                if (((ArrayType) type1).isCharArray()) {
                    new GlobalByte(name.substring(1), ((ArrayType) type1).getElementNum(), true);
                } else {
                    new GlobalWord(name.substring(1), ((ArrayType) type1).getElementNum(), true);
                }
            }
        } //需要写成寄存器那样吗？？
    }
}
