package llvm_ir;

public class Use {
    public User user;
    public Value usedValue;

    public Use(User user, Value usedValue){
        this.user = user;
        this.usedValue = usedValue;
    }

    public User getUser(){
        return user;
    }
}
