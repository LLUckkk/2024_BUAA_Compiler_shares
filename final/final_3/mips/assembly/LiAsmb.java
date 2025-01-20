package mips.assembly;

import mips.Assembly;
import mips.Register;

public class LiAsmb extends Assembly {
    private Register rd;
    private Integer immediate;

    public LiAsmb(Register rd, Integer immediate) {
        this.rd = rd;
        this.immediate = immediate;
    }

    public String toString() {
        return "li " + rd + " " + immediate;
    }


}
