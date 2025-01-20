package optimize;

import llvm_ir.*;
import llvm_ir.Module;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.TruncInstr;
import llvm_ir.instr.ZextInstr;
import mips.Register;

import java.util.*;

public class DistributeReg {
    //寄存器分配
    private Module module;
    private HashMap<Register, Value> regValueMap;
    private HashMap<Value, Register> valueRegMap;
    private ArrayList<Register> regList;

    public DistributeReg(Module module) {
        this.module = module;
        regList = new ArrayList<>();
        for (Register r : Register.values()) {
            if (r.ordinal() >= Register.T0.ordinal() && r.ordinal() <= Register.T9.ordinal()) {
                regList.add(r);
            }
        }
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            BasicBlock entryBlock = f.getBlocks().get(0);
            valueRegMap = new HashMap<>();
            regValueMap = new HashMap<>();
            distributeForBlock(entryBlock);
            f.setValueMap(valueRegMap);
            printDebug(f);
        }
    }

    public void distributeForBlock(BasicBlock block) {
        //寄存器分配！
        ArrayList<Instr> instrList = block.getInstrList();
        HashSet<Value> def_instr = new HashSet<>(); //defined
        HashSet<Value> noUse = new HashSet<>();
        HashMap<Value, Value> lastPos = new HashMap<>(); //最后一次使用的位置

        //变量在基本块中的最后一次的位置
        for (Instr instr : instrList) {
            ArrayList<Value> ops = instr.getOperands();
            for (Value op : ops) {
                lastPos.put(op, instr);
            }
        }

        //基本快的out中没有这个operand，如果其分配了寄存器，先释放
        for (Instr instr : instrList) {
            //没有写phi指令
            ArrayList<Value> operands = instr.getOperands();
            for (Value op : operands) {
                if (lastPos.get(op).equals(instr) && !block.getOut().contains(op)) {
                    if (valueRegMap.containsKey(op)) {
                        regValueMap.remove(valueRegMap.get(op));
                        noUse.add(op);
                    }
                }
            }
            if (instr.willUse() && !(instr instanceof AllocaInstr && ((AllocaInstr) instr).destType.isArray())
                    && !(instr instanceof ZextInstr) && !(instr instanceof TruncInstr)) {
                def_instr.add(instr);
                Register reg = distributeRegForValue(instr);
                if (!(reg == null)) {
                    if (regValueMap.containsKey(reg)) {
                        valueRegMap.remove(regValueMap.get(reg));
                    }
                    regValueMap.put(reg, instr);
                    valueRegMap.put(instr, reg);
                }
            }
        }
        //遍历这个块直接支配的节点
        ArrayList<BasicBlock> childBlocks = block.getChildBlock();
        if (childBlocks != null) {
            //说明没有子节点
            for (BasicBlock child : childBlocks) {
                HashMap<Register, Value> saveRegValueMap = new HashMap<>();
                //保留child中没有使用的寄存器
                //可以暂时释放的条件是
                //1.child中的in没有这个value
                //此时将映射关系save到map中，然后释放寄存器
                //为child分配后在回复 (这个地方怎么恢复的？好奇）
                for (Register register : regValueMap.keySet()) {
                    Value valueInReg = regValueMap.get(register);
                    if (!child.getIn().contains(valueInReg)) {
                        saveRegValueMap.put(register, valueInReg);
                    }
                }

                for (Register register : saveRegValueMap.keySet()) {
                    regValueMap.remove(register);
                }


                //DFS为child进行寄存器分配
                distributeForBlock(child);
                //恢复
                for (Register reg : saveRegValueMap.keySet()) {
                    regValueMap.put(reg, saveRegValueMap.get(reg));
                }
            }
        }

        //释放定义变量所占用的寄存器 为啥？
        for (Value value : def_instr) {
            if (valueRegMap.containsKey(value)) {
                regValueMap.remove(valueRegMap.get(value));
            }
        }

        //noUse里面存放的：out中没有的value，但是也不是在该基本快中定义的，
        for (Value value : noUse) {
            if (valueRegMap.containsKey(value) && !def_instr.contains(value)) {
                regValueMap.put(valueRegMap.get(value), value);
            }
        } //不懂这里为啥要恢复
    }

    public Register distributeRegForValue(Value value) {
        HashSet<Register> distributedRegs = new HashSet<>(regValueMap.keySet());
        for (Register reg : regList) {
            if (!distributedRegs.contains(reg)) {
                return reg;
            }
        }
        return regList.iterator().next();
    }

    public void printDebug(Function f) {
        ArrayList<Value> values = new ArrayList<>(valueRegMap.keySet());
        Collections.sort(values, Comparator.comparing(Value::getName));
        System.out.println("\n\n" + f.getName() + " : " + values.size());
        for (Value v : values) {
            Register reg = valueRegMap.get(v);
            System.out.println(v.getName() + " ==> " + reg.toString());
        }
    }
}
