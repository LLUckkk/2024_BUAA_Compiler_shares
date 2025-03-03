package optimize;

import llvm_ir.BasicBlock;
import llvm_ir.Function;
import llvm_ir.Instr;
import llvm_ir.Module;
import llvm_ir.instr.BrBranchInstr;
import llvm_ir.instr.BrJumpInstr;

import java.util.*;

public class CFG {
    private Module module;

    //流图记录了前驱和后继的集合
    private HashMap<BasicBlock, ArrayList<BasicBlock>> preMap;
    private HashMap<BasicBlock, ArrayList<BasicBlock>> sucMap;
    //dom记录了每个节点的支配节点集合，也就是每个节点支配了哪些块
    private HashMap<BasicBlock, ArrayList<BasicBlock>> domMap;
    //直接支配，键值是被支配的块，值是直接支配它的块
    private HashMap<BasicBlock, BasicBlock> parentMap;
    //domiChildren记录了每个节点的直接支配节点集合
    private HashMap<BasicBlock, ArrayList<BasicBlock>> childMap;
    //DF记录每个节点的支配边界
    private HashMap<BasicBlock, ArrayList<BasicBlock>> DFMap;


    public CFG(Module module) {
        this.module = module;
    }

    public void optimize() {
        LinkedList<Function> functions = module.getFunctions();
        for (Function f : functions) {
            preMap = new HashMap<>();
            sucMap = new HashMap<>();
            domMap = new HashMap<>();
            parentMap = new HashMap<>();
            childMap = new HashMap<>();
            DFMap = new HashMap<>();
            for (BasicBlock block : f.getBlocks()) {
                preMap.put(block, new ArrayList<>());
                sucMap.put(block, new ArrayList<>());
                domMap.put(block, new ArrayList<>());
                parentMap.put(block, null);
                childMap.put(block, new ArrayList<>());
                DFMap.put(block, new ArrayList<>());
            }
            genCFG(f);
            genDom(f);
            genIDom(f);
            genDF(f);
        }
    }

    public void genCFG(Function f) {
        //得到CFG图
        //根据基本块跳转指令来记录每一个基本块的next和prev基本块就可以
        LinkedList<BasicBlock> blocks = f.getBlocks();
        for (BasicBlock block : blocks) {
            Instr lastInstr = block.getLastInstr();
            if (lastInstr instanceof BrJumpInstr) {
                //lastInstr = block.getFirstBrInstr();
                lastInstr = block.getLastInstr();
                BasicBlock destBlock = ((BrJumpInstr) lastInstr).getDestBlock();
                preMap.get(destBlock).add(block);
                sucMap.get(block).add(destBlock);
            } else if (lastInstr instanceof BrBranchInstr) {
                lastInstr = block.getFirstBrInstr();
                BasicBlock trueBlock = ((BrBranchInstr) lastInstr).getTrueBlock();
                BasicBlock falseBlock = ((BrBranchInstr) lastInstr).getFalseBlock();
                preMap.get(trueBlock).add(block);
                preMap.get(falseBlock).add(block);
                sucMap.get(block).add(trueBlock);
                sucMap.get(block).add(falseBlock);
            }
        }
        for (BasicBlock block : blocks) {
            block.setForeBlocks(preMap.get(block));
            block.setSubBlocks(sucMap.get(block));
        }
        f.setForeMap(preMap);
        f.setSubMap(sucMap);
        printCFG(f);
    }

    public void genDom(Function f) {
        LinkedList<BasicBlock> blocks = f.getBlocks();
        BasicBlock entry = f.getBlocks().get(0);
        for (BasicBlock block : blocks) {
            //这里的block是支配别的block的，domiMap放的是这个块支配了谁，而不是被谁支配
            HashSet<BasicBlock> notDomedByBlock = new HashSet<>();
            DFSForNotDomed(entry, block, notDomedByBlock);
            ArrayList<BasicBlock> domiList = new ArrayList<>();
            for (BasicBlock bb : blocks) {
                if (!notDomedByBlock.contains(bb)) {
                    domiList.add(bb);
                }
            }
            domMap.put(block, domiList);
            block.setDomBlocks(domiList);
        }
    }

    public void DFSForNotDomed(BasicBlock entry, BasicBlock dest, HashSet<BasicBlock> notDomed) {
        //System.out.println("in this dfs: "+ entry.getName());
        if (dest.equals(entry)) {
            return;
        } else {
            notDomed.add(entry);
            ArrayList<BasicBlock> subList = entry.getSubBlocks();
            for (BasicBlock bb : subList) {
                if (!notDomed.contains(bb)) {
                    DFSForNotDomed(bb, dest, notDomed);
                }
            }
            //System.out.println("this dfs success"+ entry.getName());
        }
    }

    //这里的domiParent和domiChildren
    public void genIDom(Function function) {
        //直接支配节点
        LinkedList<BasicBlock> blocks = function.getBlocks();
        for (BasicBlock block : blocks) {
            ArrayList<BasicBlock> domiList = block.getDomBlocks();
            //这里的domedBlock是所有block支配的基本快
            //现在要找的是block的直接支配节点，
            // 也就是说，对于block的每个支配节点domedBlock
            //如果block不严格支配任何严格支配domedBlock的节点
            //那么block就直接支配domedBlock
            //可以临时获得一个严格支配domedBlock的集合
            for (BasicBlock domedBlock : domiList) {
                if (isIDom(block, domedBlock)) {
                    parentMap.put(domedBlock, block);
                    childMap.get(block).add(domedBlock);
                }
            }
        }
        //写入基本块
        for (BasicBlock block : blocks) {
            block.setParentBlock(parentMap.get(block));
            block.setChildBlocks(childMap.get(block));
        }
        //写入函数
        function.setDomiParent(parentMap);
        function.setDomiChildren(childMap);
    }

    public boolean isSDom(BasicBlock block, BasicBlock domedBlock) {
        return !block.equals(domedBlock);
    }

    //1.block和domedBlock必须严格支配,也就是二者不相等
    //2.同时A不严格支配任何严格支配B的节点
    //因此，遍历A支配的所有节点，找到不与A和B相同的节点
    //在看这个节点的支配节点中是否有B，如果有的话，说明不符合条件，return false
    public boolean isIDom(BasicBlock block, BasicBlock domedBlock) {
        ArrayList<BasicBlock> domedByBlock = block.getDomBlocks();
        if (!domedByBlock.contains(domedBlock) || block.equals(domedBlock)) {
            return false;
        }
        for (BasicBlock inter : domedByBlock) {
            if (!(inter.equals(block)) && !(inter.equals(domedBlock)) && inter.getDomBlocks().contains(domedBlock)) {
                return false;
            }
        }
        return true;
    }


    //计算DF支配边界
    public void genDF(Function f) {
        //用于生成phi指令
        LinkedList<BasicBlock> blocks = f.getBlocks();
        for (Map.Entry<BasicBlock, ArrayList<BasicBlock>> entry : sucMap.entrySet()) {
            BasicBlock block = entry.getKey();
            ArrayList<BasicBlock> subList = entry.getValue();
            for (BasicBlock sub : subList) {
                BasicBlock tmp = block;
                //block对应a
                //sub对应b
                //tmp对应x
                //检查是否是严格支配
                while (!tmp.getDomBlocks().contains(sub) || tmp.equals(sub)) {
                    DFMap.get(tmp).add(sub);
                    tmp = tmp.getParentBlock();
                }
            }
        }
        for (BasicBlock block : blocks) {
            block.setDFBlocks(DFMap.get(block));
        }
    }

    //打印CFG图：
    public void printCFG(Function f) {
        LinkedList<BasicBlock> blocks = f.getBlocks();
        for (BasicBlock block : blocks) {
            ArrayList<BasicBlock> foreList = preMap.get(block);
            System.out.print(block.getName() + " <== ");
            if (foreList.isEmpty()) {
                System.out.println("null");
            } else {
                for (BasicBlock fore : foreList) {
                    System.out.print(fore.getName() + " ");
                }
                System.out.println();
            }
            ArrayList<BasicBlock> subList = sucMap.get(block);
            if (subList.isEmpty()) {
                System.out.println("null");
            } else {
                System.out.print(block.getName() + " ==> ");
                for (BasicBlock sub : subList) {
                    System.out.print(sub.getName() + " ");
                }
                System.out.println();
            }
        }
    }
}
