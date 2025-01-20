package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Module;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ActiveAnalysis {
    private Module module;
    private HashMap<BasicBlock, HashSet<Value>> blockInMap;
    private HashMap<BasicBlock, HashSet<Value>> blockOutMap;

    public ActiveAnalysis(Module module) {
        this.module = module;
        blockInMap = new HashMap<>();
        blockOutMap = new HashMap<>();
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            LinkedList<BasicBlock> blocks = f.getBlocks();
            for (BasicBlock b : blocks) {
                blockInMap.put(b, new HashSet<>());
                blockOutMap.put(b, new HashSet<>());
            }
        }
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            LinkedList<BasicBlock> blocks = f.getBlocks();
            for (BasicBlock b : blocks) {
                b.createDefUse();
            }
            createInOut(f);
        }
        printDebugInfo();
    }

    public void createInOut(Function f) {
        LinkedList<BasicBlock> blocks = f.getBlocks();
        boolean notChange1 = false;
        boolean notChange2 = false;
        while (!(notChange1 && notChange2)) { //标记inmap和outMap是否有变化
            //System.out.println("here!!");
            //倒序计算in out，
            notChange1 = true;
            notChange2 = true;
            int size = blocks.size();
            for (int i = size - 1; i >= 0; i--) {
                BasicBlock b = blocks.get(i);
                HashSet<Value> outOfb = unionIn(b);
//                if (blockOutMap.containsKey(b)) {
//                    notChange1 = blockOutMap.get(b).equals(outOfb);
//                }
                blockOutMap.put(b, outOfb);
                HashSet<Value> defOfb = b.getDef();
                HashSet<Value> useOfb = b.getUse();
                HashSet<Value> newIn = new HashSet<>(outOfb);
                newIn.removeAll(defOfb);
                newIn.addAll(useOfb); //inofb = useofb U (outofb - defofb) 也就是新的In
                HashSet<Value> prevIn = blockInMap.get(b);
                if (!prevIn.equals(newIn)) {
                    blockInMap.put(b, newIn);
                    notChange2 = false;
                }
            }
        }
        for (BasicBlock block : blocks) {
            block.setIn(blockInMap.get(block));
            block.setOut(blockOutMap.get(block));
        }
    }

    public HashSet<Value> unionIn(BasicBlock b) {
        //计算b的所有后继block的in的并集
        //该后继在CFG中计算得到
        //如果没有后继？
        HashSet<Value> outOfb = new HashSet<>();
        ArrayList<BasicBlock> subBlocks = b.getSubBlocks();
        //妈呀main没有后继吗？
        if (subBlocks == null) {
            //没有后继节点，返回
            return outOfb;
        }
        for (BasicBlock subBlock : subBlocks) {
            outOfb.addAll(blockInMap.get(subBlock));
        }
        return outOfb;
    }

    public void printDebugInfo() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            LinkedList<BasicBlock> blocks = f.getBlocks();
            for (BasicBlock block : blocks) {
                System.out.println("\n");
                System.out.println(block.getName());
                System.out.print("use:");
                for (Value value : block.getUse()) {
                    System.out.println(value.getName() + " ");
                }
                System.out.println();
                System.out.print("def:");
                for (Value value : block.getDef()) {
                    System.out.println(value.getName() + " ");
                }
                System.out.println();
                System.out.print("in:");
                for (Value value : block.getIn()) {
                    System.out.println(value.getName() + " ");
                }
                System.out.println();
                System.out.print("out:");
                for (Value value : block.getOut()) {
                    System.out.println(value.getName() + " ");
                }
            }
        }//out有问题呀
    }
}
