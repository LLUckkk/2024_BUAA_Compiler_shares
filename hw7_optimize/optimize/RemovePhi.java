package optimize;


import llvm_ir.*;
import llvm_ir.Module;
import llvm_ir.instr.*;
import mips.Register;

import java.util.*;

public class RemovePhi {
    public Module module;

    public RemovePhi(Module module) {
        this.module = module;
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            //删除phi节点
            transfer2Pcopy(f);
            //增加move
            transfer2Move(f);
        }
    }

    public void transfer2Pcopy(Function f) {
        ArrayList<BasicBlock> blocks = new ArrayList<>(f.getBlocks());
        for (BasicBlock block : blocks) {
            if (!(block.getInstrList().getFirst() instanceof Phi)) {
                continue;
            }
            //此时block中必定含有phi指令
            ArrayList<BasicBlock> foreBlocks = block.getForeBlocks();
            ArrayList<Pcopy> pcopyList = new ArrayList<>();
            //遍历block的前驱，有多少前驱加多少copy
//            for (BasicBlock foreBlock : foreBlocks) {
//                Pcopy pcopy = new Pcopy(LLVMManager.getInstance().genVarNameForPhi(f));
//                pcopyList.add(pcopy);
//            }
            foreBlocks.forEach((x) -> pcopyList.add(new Pcopy(LLVMManager.getInstance().genVarNameForPhi(f))));
            for (int i = 0; i < foreBlocks.size(); i++) {
                BasicBlock foreBlock = foreBlocks.get(i);
                Pcopy pcopy = pcopyList.get(i);
                if (foreBlock.getSubBlocks().size() == 1) {
                    insertOnePcopy(pcopy, foreBlock);
                } else {
                    insertBlockPcopy(pcopy, foreBlock, block);
                }
            }
            //遍历所有的phi指令，每个phi的option放到对应的pcopy中
            //除去未定义
            Iterator<Instr> iterator = block.getInstrList().iterator();
            while (iterator.hasNext()) {
                Instr instr = iterator.next();
                if (instr instanceof Phi) {
                    ArrayList<Value> options = ((Phi) instr).getOptions();
                    for (int i = 0; i < options.size(); i++) {
                        Value value = options.get(i);
                        if (!(value instanceof Undefined)) {
                            pcopyList.get(i).addCopy(value, instr);
                        }
                    }
                    iterator.remove();
                }
            }

        }
    }

    public void transfer2Move(Function f) {
        LinkedList<BasicBlock> blocks = f.getBlocks();
        for (BasicBlock block : blocks) {
            LinkedList<Instr> instrs = block.getInstrList();
            if (instrs.size() >= 2 && instrs.get(instrs.size() - 2) instanceof Pcopy) {
                Pcopy pcopy = (Pcopy) instrs.get(instrs.size() - 2);
                instrs.remove(instrs.size() - 2);
                LinkedList<MoveInstr> moves = transfer(pcopy);
                for (MoveInstr move : moves) {
                    //Instr brInstr = block.getFirstBrInstr();
                    instrs.add(instrs.size() - 1, move);
                    move.setParent(block);
                }
            }
        }
    }

    public LinkedList<MoveInstr> transfer(Pcopy pcopy) {
        ArrayList<Value> dests = pcopy.getDests();
        ArrayList<Value> froms = pcopy.getFroms();
        Function f = pcopy.getParentBlock().getParentFunction();
        HashMap<Value, Register> varRegMap = f.getValueMap();
        LinkedList<MoveInstr> moves = new LinkedList<>();

        for (int i = 0; i < dests.size(); i++) {
            MoveInstr move = new MoveInstr(LLVMManager.getInstance().genVarNameForPhi(f),
                    dests.get(i), froms.get(i));
            moves.add(move);
        }

        //循环赋值,要实现move的并行执行
        ArrayList<MoveInstr> tmpList = new ArrayList<>();
        HashSet<Value> record = new HashSet<>();
        int size = moves.size();
        for (int i = 0; i < size; i++) {
            Value value = moves.get(i).getDest();
            if (!(value instanceof Constant) && !record.contains(value)) {
                //检查后续value是否同时是某一个的src，若是则有循环赋值
                boolean loopFlag = false;
                for (int j = i + 1; j < moves.size(); j++) {
                    if (moves.get(j).getSrc().equals(value)) {
                        loopFlag = true;
                        break;
                    }
                }
                if (loopFlag) {
                    Value interValue = new Value(value.getType(), value.getName() + "_tmp");
                    for (MoveInstr move : moves) {
                        if (move.getSrc().equals(interValue)) {
                            move.setSrc(interValue);
                        }
                    }
                    MoveInstr move = new MoveInstr(LLVMManager.getInstance().genVarNameForPhi(f), interValue, value);
                    tmpList.add(move);
                }
                record.add(value);
            }
        }

        //解决寄存器冲突
        record = new HashSet<>();
        size = moves.size();
        for (int i = size - 1; i >= 0; i--) {
            Value value = moves.get(i).getSrc();
            if (!(value instanceof Constant) && !record.contains(value)) {
                boolean conflictFlag = false;
                for (int j = 0; j < i; j++) {
                    if (varRegMap != null && varRegMap.get(value) != null) {
                        if (varRegMap.get(moves.get(j).getDest()) == varRegMap.get(value)) {
                            conflictFlag = true;
                            break;
                        }
                    }
                }
                if (conflictFlag) {
                    Value interValue = new Value(value.getType(), value.getName() + "_inter");
                    for (MoveInstr move : moves) {
                        if (move.getSrc().equals(value)) {
                            move.setSrc(interValue);
                        }
                    }
                    MoveInstr move = new MoveInstr(LLVMManager.getInstance().genVarNameForPhi(f), interValue, value);
                    tmpList.add(move);
                }
                record.add(value);
            }
        }
        //将临时变量的move语句插在move的最前面
        for (MoveInstr move : tmpList) {
            moves.addFirst(move);
        }
        return moves;
    }


    public void insertOnePcopy(Pcopy pcopy, BasicBlock block) {
        LinkedList<Instr> instrs = block.getInstrList();
        Instr lastInstr = instrs.getLast(); //注意这个坑点
        //插在第一个br之前
        assert (lastInstr instanceof BrBranchInstr) || (lastInstr instanceof BrJumpInstr);
        instrs.add(instrs.indexOf(lastInstr), pcopy);
        pcopy.setParent(block);
    }

    public void insertBlockPcopy(Pcopy pcopy, BasicBlock foreBlock, BasicBlock subBlock) {
        Function f = foreBlock.getParentFunction();
        BasicBlock interBlock = new BasicBlock(LLVMManager.getInstance().genBlockName());
        interBlock.setParentFunction(f);
        //将中间块插在后继的前面
        f.getBlocks().add(f.getBlocks().indexOf(subBlock), interBlock);
        //插入copy
        interBlock.addInstr(pcopy);
        pcopy.setParent(interBlock);
        //修改跳转关系
        BrBranchInstr brInstr = (BrBranchInstr) foreBlock.getLastInstr();
        BasicBlock trueBlock = brInstr.getTrueBlock();
        if (subBlock.equals(trueBlock)) {
            brInstr.setTrueBlock(interBlock);
            String name = LLVMManager.getInstance().genVarNameForPhi(f);
            BrJumpInstr jump = new BrJumpInstr(name, subBlock);
            interBlock.addInstr(jump);
            jump.setParent(interBlock);
        } else {
            brInstr.setFalseBlock(interBlock);
            String name = LLVMManager.getInstance().genVarNameForPhi(f);
            BrJumpInstr jump = new BrJumpInstr(name, subBlock);
            interBlock.addInstr(jump);
            jump.setParent(interBlock);
        }
        //修改前驱后继关系
        foreBlock.getSubBlocks().add(foreBlock.getSubBlocks().indexOf(subBlock), interBlock);
        foreBlock.getSubBlocks().remove(subBlock);
        subBlock.getForeBlocks().add(subBlock.getForeBlocks().indexOf(foreBlock), interBlock);
        subBlock.getForeBlocks().remove(foreBlock);
        //interBlock补充自身的前驱后继关系
        interBlock.setSubBlocks(new ArrayList<>());
        interBlock.getSubBlocks().add(subBlock);
        interBlock.setForeBlocks(new ArrayList<>());
        interBlock.getForeBlocks().add(foreBlock);
    }
}
