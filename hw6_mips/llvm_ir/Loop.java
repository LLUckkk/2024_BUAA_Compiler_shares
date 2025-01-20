package llvm_ir;

public class Loop {
    private BasicBlock headBlock;
    private BasicBlock followingBlock;
    private BasicBlock bodyBlock;

    public Loop(BasicBlock headBlock, BasicBlock followingBlock, BasicBlock bodyBlock) {
        this.headBlock = headBlock;
        this.followingBlock = followingBlock;
        this.bodyBlock = bodyBlock;
    }

    public BasicBlock getFollowingBlock(){
        return followingBlock;
    }

    public BasicBlock getHeadBlock(){
        return headBlock;
    }

}
