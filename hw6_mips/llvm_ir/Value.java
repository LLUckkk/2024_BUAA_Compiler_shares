package llvm_ir;

import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;

import java.util.ArrayList;

public class Value {

    protected LLVMType type;
    protected String name;
    protected ArrayList<Use> useList;

    public Value(LLVMType type, String name) {
        this.type = type;
        this.name = name;
        this.useList = new ArrayList<>();
    }

    public void addUse(User user) {
        Use use = new Use(user, this);
        useList.add(use);
    }

    public LLVMType getType() {
        return type;
    }

    public boolean isBase() {
        return type == BaseType.CHAR8 || type == BaseType.INT32;
    }

    public void setType(LLVMType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void toMips(){

    } //覆写
}
