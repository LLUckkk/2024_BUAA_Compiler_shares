package llvm_ir.instr;

import llvm_ir.*;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;

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
}
