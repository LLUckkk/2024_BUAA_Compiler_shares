package mips.assembly;

import llvm_ir.instr.CalcInstr;
import mips.Assembly;
import mips.Register;

public class MDAsmb extends Assembly {
    public enum MDop {
        MULT, DIV
    }

    private MDop mdop;
    private Register rs, rt;

    public MDAsmb(MDop mdop, Register rs, Register rt) {
        this.mdop = mdop;
        this.rs = rs;
        this.rt = rt;
    }

    public String toString() {
        return mdop.toString().toLowerCase() + " " + rs.toString() + " " + rt.toString();
    }
}
