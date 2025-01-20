package mips.assembly;

import mips.Assembly;
import mips.Register;

public class BranchAsmb extends Assembly {
    public enum BrOp {
        BNE, BEQ
    }

    private BrOp brOp;
    private Register rs, rt;
    private String label;

    public BranchAsmb(BrOp brOp, Register rs, Register rt, String label) {
        this.brOp = brOp;
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    public String toString() {
        return brOp.toString().toLowerCase() + " " +
                rs.toString() + ", " + rt.toString() + ", " + label;
    }
}
