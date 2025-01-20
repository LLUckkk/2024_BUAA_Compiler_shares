package mips.assembly;

import mips.Assembly;
import mips.Register;

public class JumpAsmb extends Assembly {
    public enum JumpOp {
        J, JAL, JR
    }

    private JumpOp op;
    private String destLabel;
    private Register reg;

    public JumpAsmb(JumpOp op, String destLabel) {
        this.op = op;
        this.destLabel = destLabel;
        this.reg = null;
    }

    public JumpAsmb(JumpOp op, Register reg) {
        this.op = op;
        this.reg = reg;
        this.destLabel = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(op.toString().toLowerCase());
        sb.append(" ");
        if (reg != null) {
            sb.append(reg);
        } else {
            sb.append(destLabel);
        }
        sb.append("\n");
        return sb.toString();
    }
}
