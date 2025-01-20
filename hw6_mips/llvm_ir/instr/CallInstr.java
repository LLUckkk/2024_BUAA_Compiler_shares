package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import mips.MipsManager;
import mips.Register;
import mips.assembly.*;

import java.util.ArrayList;

public class CallInstr extends Instr {
    public CallInstr(String name, Function destFunc, ArrayList<Value> RParams) {
        super(destFunc.getReturnType(), name, InstrType.CALL);
        addOperand(destFunc);
        for (Value v : RParams) {
            addOperand(v);
        }
    }

    public Function getDestFunc() {
        return (Function) operands.get(0);
    }

    public ArrayList<Value> getRParams() {
        ArrayList<Value> RParams = new ArrayList<>();
        for (int i = 1; i < operands.size(); i++) {
            RParams.add(operands.get(i));
        }
        return RParams;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getType() == BaseType.VOID) {
            sb.append("call void ");
        } else {
            sb.append(name);
            sb.append(" = call ");
            sb.append(getDestFunc().getReturnType().toString());
            sb.append(" ");
        }
        sb.append(getDestFunc().getName());
        sb.append("(");
        //下面的参数应该是实参而不是形参
        ArrayList<Value> RParams = getRParams();
        for (int i = 0; i < RParams.size(); i++) {
            Value p = RParams.get(i);
            sb.append(p.getType());
            sb.append(" ");
            sb.append(p.getName());
            if (i != RParams.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public void toMips() {
        super.toMips();
        //调用参数，注意有以下几个步骤：
        //获取必要数据
        Function destFunc = getDestFunc();
        ArrayList<Value> RParams = getRParams();
        ArrayList<Register> distributedRegs = MipsManager.getInstance().findDistributedRegs();
        int curOffset = MipsManager.getInstance().getCurOffset();
        if ((-curOffset) % 4 != 0) {
            curOffset -= 4 - (-curOffset) % 4;
        }
        //函数序言，保存寄存器以及ra
        int cnt = 0;
        for (Register reg : distributedRegs) {
            cnt++;
            new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset - cnt * 4, reg);
        }
        new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset - cnt * 4 - 4, Register.SP);
        new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP, curOffset - cnt * 4 - 8, Register.RA);
        //实参放入寄存器或者堆栈
        cnt = 0;
        int paramNum = RParams.size();
        int regNum = distributedRegs.size();
        for (Value value : RParams) {
            cnt++;
            //原版本写的是a1-a3
            if (cnt <= 4 && MipsManager.getInstance().usingRegs()) {
                Register paramReg;
                if (cnt == 1) {
                    paramReg = Register.A0;
                } else if (cnt == 2) {
                    paramReg = Register.A1;
                } else if (cnt == 3) {
                    paramReg = Register.A2;
                } else {
                    paramReg = Register.A3;
                }
                if (value instanceof Constant) { //常量
                    new LiAsmb(paramReg, ((Constant) value).getValue());
                } else if (MipsManager.getInstance().findReg(value) != null) {
                    Register fromReg = MipsManager.getInstance().findReg(value);
                    if (value instanceof Param) {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP,
                                curOffset - (distributedRegs.indexOf(fromReg) + 1) * 4, paramReg);
                    } else {
                        new MoveAsmb(paramReg, fromReg);
                    }
                } else {
                    int paramOffset = MipsManager.getInstance().findOffset(value);
                    if (value.getType().isChar8()) {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP, paramOffset, paramReg);
                    } else {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, paramOffset, paramReg);
                    }
                }
            } else {
                //直接放入堆栈里面
                //完全不理解这个地方的逻辑。。应该要结合例子来看
                Register tempReg = Register.K0;
                if (value instanceof Constant) {
                    new LiAsmb(tempReg, ((Constant) value).getValue());
                } else if (MipsManager.getInstance().findReg(value) != null) {
                    Register fromReg = MipsManager.getInstance().findReg(value);
                    if (value instanceof Param) {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP,
                                curOffset - (distributedRegs.indexOf(fromReg) + 1) * 4, tempReg);
                    } else {
                        tempReg = fromReg;
                    }
                } else {
                    if (value.getType().isChar8()) {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LB, Register.SP,
                                MipsManager.getInstance().findOffset(value), tempReg);
                    } else {
                        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP,
                                MipsManager.getInstance().findOffset(value), tempReg);
                    }
                }
                new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP,
                        curOffset - regNum * 4 - 8 - cnt * 4, tempReg);
            }
        }
        //调用函数
        //sp值设置为被调用函数的栈底地址
        new AluAsmb(AluAsmb.AluOp.ADDI, Register.SP, Register.SP, curOffset - regNum * 4 - 8);
        //跳转
        new JumpAsmb(JumpAsmb.JumpOp.JAL, destFunc.getName().substring(1));
        //函数尾声：释放栈空间，恢复寄存器和ra寄存器
        //执行完后返回
        //先恢复sp和ra
        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, 0, Register.RA);
        new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, 4, Register.SP);
        //回复寄存器
        cnt = 0;
        for (Register reg : distributedRegs) {
            cnt++;
            new MemoryAsmb(MemoryAsmb.MemoryOp.LW, Register.SP, curOffset - cnt * 4, reg);
        }
        //返回值处理
        if (destFunc.getReturnType() != BaseType.VOID) {
            if (MipsManager.getInstance().findReg(this) != null) {
                Register destReg = MipsManager.getInstance().findReg(this);
                new MoveAsmb(destReg, Register.V0);
            } else {
                if (this.getType().isChar8()) {
                    MipsManager.getInstance().storeValue(this, 1);
                    new MemoryAsmb(MemoryAsmb.MemoryOp.SB, Register.SP,
                            MipsManager.getInstance().getCurOffset(), Register.V0);
                } else {
                    MipsManager.getInstance().storeValue(this, 4);
                    new MemoryAsmb(MemoryAsmb.MemoryOp.SW, Register.SP,
                            MipsManager.getInstance().getCurOffset(), Register.V0);
                }
            }
        }
    } //对函数调用的过程应该再理一理
}
