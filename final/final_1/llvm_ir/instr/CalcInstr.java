package llvm_ir.instr;

import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.*;

public class CalcInstr extends Instr {
    public enum Op {
        ADD, SUB, MUL, SDIV, SREM
    }

    private Op op;

    public CalcInstr(String name, Op op, Value operand1, Value operand2) {
        super(BaseType.INT32, name, InstrType.CALC);
        this.op = op;
        addOperand(operand1);
        addOperand(operand2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" = ");
        sb.append(op.toString().toLowerCase());
        sb.append(" i32 ");
        sb.append(operands.get(0).getName());
        sb.append(", ");
        sb.append(operands.get(1).getName());
        return sb.toString();
    }

    public boolean willUse() {
        return true;
    }

    public void toMips() {
        //这里是一定要放在寄存器里计算的，
        //先找到三个操作数，寄存器或者栈
        //op1是常数和op2都是常数
        //op1是常数，op2不是
        //op1不是，op2是常数
        //op1和op2都不是常数
        super.toMips();
        Value operand1 = operands.get(0);
        Value operand2 = operands.get(1);
        Register op1, op2;
        op1 = Register.K0;
        op2 = Register.K1;
        Register dest = MipsManager.getInstance().findReg(this);
        if (dest == null) {
            dest = Register.K0;
        }
        if (operand1 instanceof Constant && operand2 instanceof Constant) {
            int val1 = ((Constant) operand1).getValue();
            int val2 = ((Constant) operand2).getValue();
            createLiAsmb(op, dest, val1, val2);
        } else if (operand1 instanceof Constant) {
            int imm = ((Constant) operand1).getValue();
            new LiAsmb(op1, imm);
            if (MipsManager.getInstance().findReg(operand2) != null) {
                op2 = MipsManager.getInstance().findReg(operand2);
                createAluAsmb(op, dest, op1, op2);
            } else {
                //没有寄存器
                Integer offsetOfOp = MipsManager.getInstance().findOffset(operand2);
                if (offsetOfOp == null) {
                    MipsManager.getInstance().storeValue(operand2, 4);
                    offsetOfOp = MipsManager.getInstance().getCurOffset();
                }
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offsetOfOp, op2);
                createAluAsmb(op, dest, op1, op2);
            }
        } else if (operand2 instanceof Constant) {
            int imm = ((Constant) operand2).getValue();
            new LiAsmb(op2, imm);
            if (MipsManager.getInstance().findReg(operand1) != null) {
                op1 = MipsManager.getInstance().findReg(operand1);
                createAluAsmb(op, dest, op1, op2);
            } else {
                //没有寄存器
                Integer offsetOfOp = MipsManager.getInstance().findOffset(operand1);
                if (offsetOfOp == null) {
                    MipsManager.getInstance().storeValue(operand1, 4);
                    offsetOfOp = MipsManager.getInstance().getCurOffset();
                }
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offsetOfOp, op1);
                createAluAsmb(op, dest, op1, op2);
            }
        } else {
            if (MipsManager.getInstance().findReg(operand1) != null) {
                op1 = MipsManager.getInstance().findReg(operand1);
            } else {
                Integer offsetOfOp = MipsManager.getInstance().findOffset(operand1);
                if (offsetOfOp == null) {
                    MipsManager.getInstance().storeValue(operand1, 4);
                    offsetOfOp = MipsManager.getInstance().getCurOffset();
                }
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offsetOfOp, op1);
            }
            if (MipsManager.getInstance().findReg(operand2) != null) {
                op2 = MipsManager.getInstance().findReg(operand2);
            } else {
                Integer offsetOfOp = MipsManager.getInstance().findOffset(operand2);
                if (offsetOfOp == null) {
                    MipsManager.getInstance().storeValue(operand2, 4);
                    offsetOfOp = MipsManager.getInstance().getCurOffset();
                }
                new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offsetOfOp, op2);
            }
            createAluAsmb(op, dest, op1, op2);
        }
        if (MipsManager.getInstance().findReg(this) == null) {
            MipsManager.getInstance().storeValue(this, 4);
            int curOffset = MipsManager.getInstance().getCurOffset();
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, dest);
        }
    }

    public void createAluIAsmb(Op op, Register dest, Register op1, int imm) {
        Register op2;
        switch (op) {
            case ADD:
                new AluAsmb(AluAsmb.AluOp.ADDI, dest, op1, imm);
                break;
            case SUB:
                new AluAsmb(AluAsmb.AluOp.ADDI, dest, op1, -imm);
                break;
            case MUL:
                op2 = op1 == Register.K0 ? Register.K1 : Register.K0;
                new LiAsmb(op2, imm);
                new MDAsmb(MDAsmb.MDop.MULT, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFLO, dest);
                break;
            case SDIV:
                op2 = op1 == Register.K0 ? Register.K1 : Register.K0;
                new LiAsmb(op2, imm);
                new MDAsmb(MDAsmb.MDop.DIV, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFLO, dest);
                break;
            case SREM:
                op2 = op1 == Register.K0 ? Register.K1 : Register.K0;
                new LiAsmb(op2, imm);
                new MDAsmb(MDAsmb.MDop.DIV, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFHI, dest);
                break;
        }
    } //有立即数

    public void createAluAsmb(Op op, Register dest, Register op1, Register op2) {
        switch (op) {
            case ADD:
                new AluAsmb(AluAsmb.AluOp.ADDU, dest, op1, op2);
                break;
            case SUB:
                new AluAsmb(AluAsmb.AluOp.SUBU, dest, op1, op2);
                break;
            case MUL:
                new MDAsmb(MDAsmb.MDop.MULT, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFLO, dest);
                break;
            case SDIV:
                new MDAsmb(MDAsmb.MDop.DIV, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFLO, dest);
                break;
            case SREM:
                new MDAsmb(MDAsmb.MDop.DIV, op1, op2);
                new HiLoAsmb(HiLoAsmb.HLop.MFHI, dest);
                break;
        }

    } //没有立即数

    public void createLiAsmb(Op op, Register dest, int val1, int val2) {
        switch (op) {
            case ADD:
                new LiAsmb(dest, val1 + val2);
                break;
            case SUB:
                new LiAsmb(dest, val1 - val2);
                break;
            case MUL:
                new LiAsmb(dest, val1 * val2);
                break;
            case SDIV:
                new LiAsmb(dest, val1 / val2);
                break;
            case SREM:
                new LiAsmb(dest, val1 % val2);
                break;
        }
    }
}
