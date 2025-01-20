package mips.assembly;

import mips.Assembly;
import mips.Register;

public class HiLoAsmb extends Assembly {
    public enum HLop {
        MTLO, MTHI, MFLO, MFHI
    }

    private HLop hlOp;
    private Register rd;

    public HiLoAsmb(HLop hlOp, Register rd) {
        this.hlOp = hlOp;
        this.rd = rd;
    }

    public String toString() {
        return hlOp.toString().toLowerCase() + " " + rd.toString();
    }
}
