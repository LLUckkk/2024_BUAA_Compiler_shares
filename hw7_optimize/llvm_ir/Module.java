package llvm_ir;

import llvm_ir.instr.*;
import llvm_ir.type.OtherType;
import mips.Register;
import mips.assembly.JumpAsmb;
import mips.assembly.LabelAsmb;
import mips.assembly.LiAsmb;
import mips.assembly.SyscallAsmb;

import java.util.ArrayList;
import java.util.LinkedList;

public class Module extends Value {
    //一些基本的属性
    private ArrayList<String> declarations;
    private LinkedList<StringLiteral> stringLiterals;
    private LinkedList<GlobalVar> globalVariables;
    private LinkedList<Function> functions;

    public Module() {
        super(OtherType.MODULE, "module");
        this.declarations = new ArrayList<>();
        this.stringLiterals = new LinkedList<>();
        this.globalVariables = new LinkedList<>();
        this.functions = new LinkedList<>();
        //不是很懂为啥用LinkedList
        //关于I/O函数的相关声明，直接加入
        this.declarations.add(GetIntInstr.getDeclareCode());
        this.declarations.add(PutIntInstr.getDeclareCode());
        this.declarations.add(GetCharInstr.getDeclareCode());
        this.declarations.add(PutCharInstr.getDeclareCode());
        this.declarations.add(PutStrInstr.getDeclareCode());
    }

    //调用的需要存在这一类的数据的方法
    public void addGlobalVar(GlobalVar globalVar) {
        globalVariables.add(globalVar);
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public void addStringLiteral(StringLiteral stringLiteral) {
        stringLiterals.add(stringLiteral);
    }

    public LinkedList<Function> getFunctions() {
        return functions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String decl : declarations) {
            sb.append(decl);
            sb.append("\n");
        }
        sb.append("\n\n\n"); //分隔符
        for (StringLiteral str : stringLiterals) {
            sb.append(str.toString());
            sb.append("\n");
        }
        sb.append("\n\n\n");
        for (GlobalVar globalVar : globalVariables) {
            sb.append(globalVar.toString());
            sb.append("\n");
        }
        sb.append("\n\n\n");
        for (Function function : functions) {
            sb.append(function.toString());
            sb.append("\n");
        }
        return sb.toString();
    }//分隔符

    public void toMips() {
        for (GlobalVar globalVar : globalVariables) {
            globalVar.toMips();
        }
        for (StringLiteral str : stringLiterals) {
            str.toMips();
        }

        new JumpAsmb(JumpAsmb.JumpOp.JAL, "main");
        new JumpAsmb(JumpAsmb.JumpOp.J, "exit");

        for (Function function : functions) {
            function.toMips();
        }

        new LabelAsmb("exit");
        new LiAsmb(Register.V0, 10);
        new SyscallAsmb();
    }
}
