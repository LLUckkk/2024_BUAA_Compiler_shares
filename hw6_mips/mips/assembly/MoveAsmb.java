package mips.assembly;

import mips.Assembly;
import mips.Register;

public class MoveAsmb extends Assembly {
    private Register dest;
    private Register src;

    public MoveAsmb(Register dest, Register src) {
        this.dest = dest;
        this.src = src;
    }

    public String toString() {
        return "move " + dest + " " + src;
    }
}
