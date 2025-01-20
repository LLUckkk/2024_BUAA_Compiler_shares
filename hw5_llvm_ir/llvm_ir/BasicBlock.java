package llvm_ir;

import llvm_ir.type.OtherType;

import java.util.LinkedList;

public class BasicBlock extends Value {
    //基本属性
    private LinkedList<Instr> instrList;
    private Function parent; //所属函数

    public BasicBlock(String name) {
        super(OtherType.FUNCTION, name);
        this.instrList = new LinkedList<>();
        this.parent = null;
        IRBuilder.getInstance().addBasicBlock(this);
    }

    public void setParent(Function parent) {
        this.parent = parent;
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
}
