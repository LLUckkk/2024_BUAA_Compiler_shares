package llvm_ir;

import llvm_ir.type.OtherType;
import mips.assembly.LabelAsmb;

import java.util.ArrayList;

public class BasicBlock extends Value {
    //基本属性
    private ArrayList<Instr> instrList;
    private Function parentFunction; //所属函数

    public BasicBlock(String name) {
        super(OtherType.FUNCTION, name);
        this.instrList = new ArrayList<>();
        this.parentFunction = null;
        LLVMManager.getInstance().addBasicBlock(this);
    }

    public void setParentFunction(Function parentFunction) {
        this.parentFunction = parentFunction;
    }

    public void addInstr(Instr instr) {
        this.instrList.add(instr);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((!name.equals("@main")) && (!instrList.isEmpty())) {
            sb.append(name);
            sb.append(":\n");
        }
        for (Instr instr : instrList) {
            sb.append(instr.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public Function getParentFunction() {
        return parentFunction;
    }

    public void toMips() {
        if (!name.equals("@main")) {
            new LabelAsmb(name);
        }
        for (Instr instr : instrList) {
            instr.toMips();
        }
    }
}
