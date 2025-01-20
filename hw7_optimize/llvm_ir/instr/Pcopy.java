package llvm_ir.instr;

import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;

import java.util.ArrayList;

public class Pcopy extends Instr {
    private ArrayList<Value> dests;
    private ArrayList<Value> froms;

    public Pcopy(String name) {
        super(BaseType.VOID, name, InstrType.PCOPY);
        this.dests = new ArrayList<>();
        this.froms = new ArrayList<>();
    }

    public void addCopy(Value from, Value dest) {
        this.dests.add(dest);
        this.froms.add(from);
    }

    public ArrayList<Value> getDests() {
        return this.dests;
    }

    public ArrayList<Value> getFroms() {
        return this.froms;
    }

}
