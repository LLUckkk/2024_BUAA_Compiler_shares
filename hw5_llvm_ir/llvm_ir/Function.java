package llvm_ir;

import llvm_ir.type.LLVMType;
import llvm_ir.type.OtherType;

import java.util.ArrayList;
import java.util.LinkedList;

public class Function extends User {
    //存储函数基本信息：
    private ArrayList<Param> params; //函数参数列表
    private LLVMType returnType; //函数返回类型
    private LinkedList<BasicBlock> blocks; // 这个含义不确定

    public Function(String name, LLVMType returnType) {
        super(OtherType.FUNCTION, name);
        this.returnType = returnType;
        this.params = new ArrayList<>();
        this.blocks = new LinkedList<>();
        if (IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addFunction(this);
        }
    }

    public void addBasicBlock(BasicBlock block) {
        blocks.add(block);
    }

    public void addParam(Param param) {
        params.add(param);
    }

    public LLVMType getReturnType() {
        return returnType;
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        sb.append(returnType.toString());
        sb.append(" ");
        sb.append(name);
        sb.append("(");
        //参数
        for (Param param : params) {
            sb.append(param.toString());
            if (!(params.indexOf(param) == params.size() - 1)) {
                sb.append(", ");
            }
        }
        sb.append(")\n");
        sb.append("{\n");
        for (BasicBlock block : blocks) {
            sb.append(block.toString());
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
