package llvm_ir.instr;

import llvm_ir.Constant;
import llvm_ir.Instr;
import llvm_ir.InstrType;
import llvm_ir.Value;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.CmpAsmb;
import mips.assembly.LiAsmb;
import mips.assembly.MemoryAsmb;

public class IcmpInstr extends Instr {
    public enum IcmpCond {
        EQ,
        NE,
        SGT,
        SGE,
        SLT,
        SLE
    }

    private IcmpCond cond;

    public IcmpInstr(String name, IcmpCond cond, Value operand1, Value operand2) {
        super(BaseType.INT1, name, InstrType.ICMP);
        this.cond = cond;
        addOperand(operand1);
        addOperand(operand2);
    }

    public String toString() {
        return name +
                " = icmp " +
                cond.toString().toLowerCase() +
                " i32 " +
                operands.get(0).getName() +
                ", " +
                operands.get(1).getName();
    }

    public void toMips() {
        super.toMips();
        Value operand1 = operands.get(0);
        Value operand2 = operands.get(1);
        Register op1 = Register.K0;
        Register op2 = Register.K1;
        Register dest = MipsManager.getInstance().findReg(this);
        if (dest == null) {
            dest = Register.K0;
        } //这个地方其实不太懂为啥是这样
        op1 = getReg(operand1, op1);
        op2 = getReg(operand2, op2);
        createIcmpAsmb(cond, op1, op2, dest);
        if (MipsManager.getInstance().findOffset(this) == null) {
            int curOffset = MipsManager.getInstance().storeValue(this, 4);
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset, dest);
        }
    }

    public void createIcmpAsmb(IcmpCond cond, Register op1, Register op2, Register dest) {
        switch (cond) {
            case EQ:
                new CmpAsmb(CmpAsmb.CmpOp.SEQ, op1, op2, dest);
                break;
            case NE:
                new CmpAsmb(CmpAsmb.CmpOp.SNE, op1, op2, dest);
                break;
            case SGT:
                new CmpAsmb(CmpAsmb.CmpOp.SGT, op1, op2, dest);
                break;
            case SGE:
                new CmpAsmb(CmpAsmb.CmpOp.SGE, op1, op2, dest);
                break;
            case SLT:
                new CmpAsmb(CmpAsmb.CmpOp.SLT, op1, op2, dest);
                break;
            case SLE:
                new CmpAsmb(CmpAsmb.CmpOp.SLE, op1, op2, dest);
                break;
        }
    }

    public Register getReg(Value operand, Register opReg) {
        if (operand instanceof Constant) {
            new LiAsmb(opReg, ((Constant) operand).getValue());
        } else if (MipsManager.getInstance().findReg(operand) != null) {
            opReg = MipsManager.getInstance().findReg(operand);
        } else {
            Integer offset = MipsManager.getInstance().loadValue(operand);
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, offset, opReg);
        }
        return opReg;
    }
}
