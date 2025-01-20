package llvm_ir;

import llvm_ir.instr.BrBranchInstr;
import llvm_ir.instr.BrJumpInstr;
import llvm_ir.type.OtherType;
import mips.assembly.LabelAsmb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BasicBlock extends Value {
    //基本属性
    private ArrayList<Instr> instrList;
    private Function parentFunction; //所属函数
    private boolean removed; //是否已被删除
    private HashSet<Value> use;
    private HashSet<Value> def;
    private HashSet<Value> in;
    private HashSet<Value> out;
    private ArrayList<BasicBlock> foreBlocks;
    private ArrayList<BasicBlock> subBlocks;
    private ArrayList<BasicBlock> domBlocks;
    private BasicBlock parentBlock;
    private ArrayList<BasicBlock> childBlocks;

    public BasicBlock(String name) {
        super(OtherType.FUNCTION, name);
        this.instrList = new ArrayList<>();
        this.parentFunction = null;
        removed = false;
        LLVMManager.getInstance().addBasicBlock(this);
    }

    public Instr getFirstBrInstr() {
        Instr instr = null;
        for (Instr instr1 : instrList) {
            if(instr1 instanceof BrJumpInstr || instr1 instanceof BrBranchInstr) {
                instr = instr1;
                break;
            }
        }
        return instr;
    }

    public void setParentFunction(Function parentFunction) {
        this.parentFunction = parentFunction;
    }

    public void setRemoved() {
        removed = true;
    }

    public void setIn(HashSet<Value> in) {
        this.in = in;
    }

    public void setOut(HashSet<Value> out) {
        this.out = out;
    }

    public void setForeBlocks(ArrayList<BasicBlock> foreBlocks) {
        this.foreBlocks = foreBlocks;
    }

    public void setSubBlocks(ArrayList<BasicBlock> subBlocks) {
        this.subBlocks = subBlocks;
    }

    public void setDomBlocks(ArrayList<BasicBlock> domBlocks) {
        this.domBlocks = domBlocks;
    }

    public void setParentBlock(BasicBlock parentBlock) {
        this.parentBlock = parentBlock;
    }

    public void setChildBlocks(ArrayList<BasicBlock> childBlocks) {
        this.childBlocks = childBlocks;
    }

    public ArrayList<BasicBlock> getChildBlock() {
        return childBlocks;
    }

    public ArrayList<BasicBlock> getDomBlocks() {
        return domBlocks;
    }

    public ArrayList<BasicBlock> getForeBlocks() {
        return foreBlocks;
    }

    public ArrayList<BasicBlock> getSubBlocks() {
        return subBlocks;
    }

    public HashSet<Value> getOut() {
        return out;
    }

    public HashSet<Value> getIn() {
        return in;
    }

    public void addInstr(Instr instr) {
        this.instrList.add(instr);
    }

    public Function getParentFunction() {
        return parentFunction;
    }

    public ArrayList<Instr> getInstrList() {
        return instrList;
    }

    public Instr getLastInstr() {
        return instrList.get(instrList.size() - 1);
    }

    public void createDefUse() {
        def = new HashSet<>();
        use = new HashSet<>();
        for (Instr instr : instrList) {
            //先使用后定义
            for (Value value : instr.operands) {
                if (!def.contains(value)) {
                    if (value instanceof Instr || value instanceof Param || value instanceof GlobalVar) {
                        use.add(value);
                    }
                }
            }
            //先定义后使用
            if (!use.contains(instr) && instr.willUse()) {
                def.add(instr);
            }
        }
    }

    public HashSet<Value> getDef() {
        return def;
    }

    public HashSet<Value> getUse() {
        return use;
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

    public void toMips() {
        if (!name.equals("@main")) {
            new LabelAsmb(name);
        }
        for (Instr instr : instrList) {
            instr.toMips();
        }
    }
}
