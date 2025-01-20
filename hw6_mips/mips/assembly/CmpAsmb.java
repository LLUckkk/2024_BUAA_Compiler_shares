package mips.assembly;

import mips.Assembly;
import mips.Register;

public class CmpAsmb extends Assembly {
    public enum CmpOp {
        SLT, SGT,
        SLE, SGE,
        SEQ, SNE
    }

    private CmpAsmb.CmpOp cmpOp;
    private Register rs, rt, rd;

    public CmpAsmb(CmpOp cmpOp, Register rs, Register rt, Register rd) {
        this.cmpOp = cmpOp;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }

    public String toString() {
        return cmpOp.toString().toLowerCase() + " " + rd.toString() +
                ", " + rs.toString() + ", " + rt.toString();
    }
}
