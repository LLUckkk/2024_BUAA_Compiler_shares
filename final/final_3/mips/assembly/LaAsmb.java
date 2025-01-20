package mips.assembly;

import mips.Assembly;
import mips.Register;

public class LaAsmb extends Assembly {
    private Register dest;
    private String label;

    public LaAsmb(Register dest, String label) {
        this.dest = dest;
        this.label = label;
    }

    public String toString() {
        return "la " + dest.toString() + ", " + label;
    }
}
