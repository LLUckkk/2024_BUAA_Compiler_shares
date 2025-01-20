package mips;

import llvm_ir.Function;
import llvm_ir.Param;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MipsManager {
    private static MipsManager mipsBuilder = new MipsManager();

    private AssemblyTable assemblyTable;
    private Function curFunction;
    private int curOffset;
    private HashMap<Value, Integer> offsetMap;
    private HashMap<Value, Register> registerMap;
    //registerMap是优化阶段用的，暂时先不用


    private MipsManager() {
        assemblyTable = new AssemblyTable();
        curFunction = null;
    }

    public static MipsManager getInstance() {
        return mipsBuilder;
    }

    public void addDataAssembly(Assembly assembly) {
        assemblyTable.addDataAssembly(assembly);
    }

    public void addTextAssembly(Assembly assembly) {
        assemblyTable.addTextAssembly(assembly);
    }

    public void enterFunc(Function function) {
        curFunction = function;
        curOffset = 0;
        offsetMap = new HashMap<>();
        registerMap = function.getValueMap();
    }

    public void giveReg2Param(Register reg, Param param) {
        if (registerMap == null) {
            return;
        }
        registerMap.put(param, reg);
    }

    public void giveStack2Value(Value value, int offset) {
        offsetMap.put(value, offset);
    }

    public void subOffset(int delta) {
//        if (delta < curOffset) {
//            curOffset -= delta;
//        } else {
//            System.out.println("out of range in offset!!");
//        }
        curOffset -= delta;
        //assert curOffset >= 0;
    }

    public boolean isUsingReg(){
        return registerMap != null;
    }

    public int getCurOffset() {
        return curOffset;
    }

    public ArrayList<Register> findDistributedRegs() {
        if (registerMap == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(new HashSet<>(registerMap.values()));
        }
    }

    public Register findReg(Value value) {
        if (registerMap != null) {
            return registerMap.get(value);
        } else {
            //System.out.println("registerMap is null!!");
            return null;
        }
    }

    public Integer findOffset(Value value) {
        if (offsetMap != null) { //这个value没被存上
            return offsetMap.get(value);
        } else {
            //System.out.println("offsetMap is null!!");
            return null;
        }
    }

    public int storeValue(Value value, int offset) {
        if (offset == 4 || value.getType().isPointer()) {
            if ((-curOffset) % 4 != 0) {
                offset += 4 - (-curOffset) % 4;
            }
        }
        subOffset(offset);
        giveStack2Value(value, curOffset);
        return curOffset;
    } //只是建立了一个映射，并没有真的把值存到栈上，真正想存到栈上必须使用mem指令

    public boolean usingRegs() {
        return registerMap != null;
    }

    public Integer loadValue(Value value) {
        Integer offset = findOffset(value);
        if (offset == null) {
            if (value.getType().isInt32()) {
                return MipsManager.getInstance().storeValue(value, 4);
            } else {
                offset = MipsManager.getInstance().storeValue(value, 1);
            }
        }
        return offset;
    }

    public AssemblyTable getAssemblyTable() {
        return assemblyTable;
    }


}
