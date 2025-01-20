package llvm_ir;

import llvm_ir.type.LLVMType;
import llvm_ir.type.OtherType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.LabelAsmb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Function extends User {
    //存储函数基本信息：
    private ArrayList<Param> params; //函数参数列表
    private LLVMType returnType; //函数返回类型
    private LinkedList<BasicBlock> blocks; // 这个含义不确定
    private HashMap<Value, Register> valueMap;
    private HashMap<BasicBlock, ArrayList<BasicBlock>> foreMap;
    private HashMap<BasicBlock, ArrayList<BasicBlock>> subMap;
    private HashMap<BasicBlock, BasicBlock> domiParent;
    private HashMap<BasicBlock, ArrayList<BasicBlock>> domiChildren;

    public Function(String name, LLVMType returnType) {
        super(OtherType.FUNCTION, name);
        this.returnType = returnType;
        this.params = new ArrayList<>();
        this.blocks = new LinkedList<>();
        if (LLVMManager.mode == LLVMManager.AUTO_INSERT_MODE) {
            LLVMManager.getInstance().addFunction(this);
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

    public LinkedList<BasicBlock> getBlocks() {
        return blocks;
    }

    public void setValueMap(HashMap<Value, Register> valueMap) {
        this.valueMap = valueMap;
    }

    public HashMap<Value, Register> getValueMap() {
        return valueMap;
    }

    public void setForeMap(HashMap<BasicBlock, ArrayList<BasicBlock>> foreMap) {
        this.foreMap = foreMap;
    }

    public void setSubMap(HashMap<BasicBlock, ArrayList<BasicBlock>> subMap) {
        this.subMap = subMap;
    }

    public void setDomiParent(HashMap<BasicBlock, BasicBlock> domiParent) {
        this.domiParent = domiParent;
    }

    public void setDomiChildren(HashMap<BasicBlock, ArrayList<BasicBlock>> domiChildren) {
        this.domiChildren = domiChildren;
    }

//    public void toMips() {
//        //函数注意栈的分配
//        new LabelAsmb(name.substring(1));
//        MipsManager.getInstance().enterFunc(this);
//        //前四个参数跟a0-a3寄存器产生映射，剩余的放到栈里
//        //但现在还没有优化，想全部放到栈里
//        for (int i = 0; i < params.size(); i++) {
//            MipsManager.getInstance().storeValue(params.get(i), 4);
//        }
//        for (BasicBlock block : blocks) {
//            block.toMips();
//        }
//    }

    public void toMips() {
        //函数注意栈的分配
        new LabelAsmb(name.substring(1));
        MipsManager.getInstance().enterFunc(this);
        //前三个参数跟a1-a3寄存器产生映射，剩余的放到栈里
        //但现在还没有优化，想全部放到栈里
        for (int i = 0; i < params.size(); i++) {
            if (i == 0) {
                MipsManager.getInstance().giveReg2Param(Register.A1, params.get(i));
            } else if (i == 1) {
                MipsManager.getInstance().giveReg2Param(Register.A2, params.get(i));
            } else if (i == 2) {
                MipsManager.getInstance().giveReg2Param(Register.A3, params.get(i));
            }
            MipsManager.getInstance().storeValue(params.get(i), 4);
        }
        for (BasicBlock block : blocks) {
            block.toMips();
        }
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

//            if (i == 0) {
//                MipsManager.getInstance().giveReg2Param(Register.A0, params.get(i));
//            } else if (i == 1) {
//                MipsManager.getInstance().giveReg2Param(Register.A1, params.get(i));
//            } else if (i == 2) {
//                MipsManager.getInstance().giveReg2Param(Register.A2, params.get(i));
//            } else if (i == 3) {
//                MipsManager.getInstance().giveReg2Param(Register.A3, params.get(i));
//            }
//            MipsManager.getInstance().subOffset(4);
//            if (i > 3) {
//                MipsManager.getInstance().giveStack2Value(params.get(i),
//                        MipsManager.getInstance().getCurOffset());
//            }