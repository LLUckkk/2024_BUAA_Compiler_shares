package mips.assembly;

import mips.Assembly;
import mips.Register;

public class MemoryAsmb extends Assembly {
    public enum MemoryOp {
        LW, LB,
        SW, SB
    }

    private final MemoryOp op;
    private final Register base;
    private final int offset;
    private final Register rt; //dest

    public MemoryAsmb(MemoryOp op, Register base, int offset, Register rt) {
        this.op = op;
        this.base = base;
        this.offset = offset;
        this.rt = rt;
    } //为啥这有个label？？

    public String toString() {
        return op.toString().toLowerCase() + " " +
                rt +
                ", " +
                offset +
                "(" +
                base +
                ")\n";
    }
}
