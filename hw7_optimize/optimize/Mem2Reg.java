package optimize;

import llvm_ir.*;
import llvm_ir.Module;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.LoadInstr;
import llvm_ir.instr.Phi;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.PointerType;

import java.util.*;

public class Mem2Reg {
    private Module module;
    private Instr curAlloca;
    private ArrayList<Instr> useInstrs;
    private ArrayList<Instr> defInstrs;
    private ArrayList<BasicBlock> useBlocks;
    private ArrayList<BasicBlock> defBlocks;
    private Stack<Value> stack;


    public Mem2Reg(Module module) {
        this.module = module;
        this.curAlloca = null;
        this.useBlocks = null;
        this.defBlocks = null;
        this.useInstrs = null;
        this.defInstrs = null;
        this.stack = null;
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            //遍历基本块插入phi节点
            LinkedList<BasicBlock> blocks = f.getBlocks();
            for (BasicBlock block : blocks) {
                //遍历基本块中的指令
                ArrayList<Instr> instrs = new ArrayList<>(block.getInstrList());
                for (Instr instr : instrs) {
                    if (canInsertPhi(instr)) {
                        init(instr);
                        insertPhi(instr);
                        BasicBlock entry = blocks.get(0);
                        renamePhi(entry);
                    }
                }
            }
        }
    }

    public void init(Instr instr) {
        this.curAlloca = instr;
        this.useBlocks = new ArrayList<>();
        this.defBlocks = new ArrayList<>();
        this.useInstrs = new ArrayList<>();
        this.defInstrs = new ArrayList<>();
        this.stack = new Stack<>();

        //定义使用链？
        //计算出useBlock和defBlock
        ArrayList<Use> useList = instr.getUseList();
        for (Use use : useList) {
            assert use.getUser() instanceof Instr;
            Instr user = (Instr) use.getUser();
            if (user instanceof StoreInstr) {
                defInstrs.add(user);
                if (!defBlocks.contains(user.getParentBlock()) && !user.getParentBlock().isRemoved()) {
                    defBlocks.add(user.getParentBlock());
                }
            } else if (user instanceof LoadInstr) {
                useInstrs.add(user);
                if (!defBlocks.contains(user.getParentBlock()) && !user.getParentBlock().isRemoved()) {
                    useBlocks.add(user.getParentBlock());
                }
            }
        }
    }

    public boolean canInsertPhi(Instr instr) {
        //TODO:这里之前还是写了destType是Int32？
        return instr instanceof AllocaInstr && (((PointerType) instr.getType()).getDestType().isInt32() ||
                ((PointerType) instr.getType()).getDestType().isChar8());
    }


    //根据算法进行计算
    public void insertPhi(Instr instr) {
        HashSet<BasicBlock> F = new HashSet<>();
        Stack<BasicBlock> W = new Stack<>();
        for (BasicBlock block : defBlocks) {
            W.push(block);
        }
        while (!W.isEmpty()) {
            BasicBlock X = W.pop();
            ArrayList<BasicBlock> dfOfBlock = X.getDFBlocks();
            for (BasicBlock Y : dfOfBlock) {
                if (!F.contains(Y)) {
                    insertInBlock(instr, Y);
                    F.add(Y);
                    if (!defBlocks.contains(Y)) {
                        W.push(Y);
                    }
                }
            }
        }
    }

    public void insertInBlock(Instr instr, BasicBlock Y) {
        LinkedList<Instr> instrs = Y.getInstrList();
        Function parentFunc = Y.getParentFunction();
        String name = LLVMManager.getInstance().genVarNameForPhi(parentFunc);
        Instr phi = new Phi(instr.getType(), name, Y.getForeBlocks());
        instrs.addFirst(phi);
        phi.setParent(Y);
        useInstrs.add(phi);
        defInstrs.add(phi);
    }

    //通过DFS对load，store和phi进行重命名
    public void renamePhi(BasicBlock entry) {
        int cnt = 0;
        Iterator<Instr> iterator = entry.getInstrList().iterator();
        while (iterator.hasNext()) {
            Instr instr = iterator.next();
            //这里的useInstrs和defInstrs每个变量各有一个
            if (instr instanceof LoadInstr && useInstrs.contains(instr)) {
                Value value;
                if (stack.isEmpty()) {
                    value = new Undefined(instr.getType());
                } else {
                    value = stack.peek();
                }
                setNewValue(instr, value);
                iterator.remove();
            } else if (instr instanceof StoreInstr && defInstrs.contains(instr)) {
                //推入stack
                Value value = ((StoreInstr) instr).getFrom();
                stack.push(value);
                cnt++;
                iterator.remove();
            } else if (instr instanceof Phi && defInstrs.contains(instr)) {
                //推入stack
                stack.push(instr);
                cnt++;
            } else if (instr.equals(curAlloca)) {
                iterator.remove();
            }
        }

        //遍历entry的后继集合
        ArrayList<BasicBlock> subBlocks = entry.getSubBlocks();
        for (BasicBlock block : subBlocks) {
            Instr firstInstr = block.getInstrList().getFirst();
            if (firstInstr instanceof Phi && useInstrs.contains(firstInstr)) {
                Value option;
                if (stack.isEmpty()) {
                    option = new Undefined(firstInstr.getType());
                } else {
                    option = stack.peek();
                }
                ((Phi) firstInstr).addOption(entry, option);
            }
        }
        ArrayList<BasicBlock> childBlocks = entry.getChildBlock();
        for (BasicBlock block : childBlocks) {
            renamePhi(block);
        }
        for (int i = 0; i < cnt; i++) {
            stack.pop();
        } //将这个变量的value出栈
    }

    public void setNewValue(Instr instr, Value newValue) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Use> useList = instr.getUseList();
        for (Use use : useList) {
            users.add(use.getUser());
        }
        for (User user : users) {
            user.modifyOperand(instr, newValue);
        }
    }
}
