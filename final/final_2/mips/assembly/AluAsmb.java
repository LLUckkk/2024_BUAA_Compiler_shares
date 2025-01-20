package mips.assembly;

import mips.Assembly;
import mips.Register;

public class AluAsmb extends Assembly {
    public enum AluOp {
        ADD, SUB, AND, OR, NOR,
        ADDI, ADDU, SUBU,
        SLL
    }

    private AluOp aluOp;
    private Register rd, rs, rt;
    private Integer imm;

    //rd = rs + rt
    public AluAsmb(AluOp aluOp, Register rd, Register rs, Register rt) {
        this.aluOp = aluOp;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
        imm = null;
    }

    //rt = rs + imm
    public AluAsmb(AluOp aluOp, Register rt, Register rs, Integer imm) {
        this.aluOp = aluOp;
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
        this.rd = null;
    } //顺序问题！！！，对于-和mod来说，有先后顺序
    //规范ALU的参数顺序

    public AluAsmb(AluOp aluOp, Register rt, Integer imm, Register rs) {
        this.aluOp = aluOp;
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
        this.rd = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(aluOp.toString().toLowerCase());
        sb.append(" ");
        if (rd != null) {
            sb.append(rd);
            sb.append(", ");
            sb.append(rs.toString());
            sb.append(", ");
            sb.append(rt.toString());
            sb.append("\n");
        } else {
            sb.append(rt.toString());
            sb.append(", ");
            sb.append(rs.toString());
            sb.append(", ");
            sb.append(imm);
            sb.append("\n");
        }
        return sb.toString();
    }
}
