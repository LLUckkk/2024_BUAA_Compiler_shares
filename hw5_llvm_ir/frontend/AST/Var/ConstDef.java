package frontend.AST.Var;

import frontend.AST.Exp.ConstExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.ConstSymbol;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import llvm_ir.instr.TruncInstr;
import utils.Printer;
import llvm_ir.*;
import llvm_ir.initial.Initial;
import llvm_ir.instr.AllocaInstr;
import llvm_ir.instr.GEPInstr;
import llvm_ir.instr.StoreInstr;
import llvm_ir.type.ArrayType;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

public class ConstDef extends Node {
    /* constDef的文法: Ident [ '[' ConstExp ']' ] '=' ConstInitVal*/
    private boolean isChar;
    private ConstSymbol constSymbol;

    public ConstDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        isChar = false;
    }

    public void setIsChar() {
        isChar = true;
    }

    public ConstSymbol createSymbol() {
        TokenNode ident = (TokenNode) children.get(0);
        String symbolName = ident.getToken().getValue();
        ValueType valueType;
        Initial initial;
        LLVMType initialType;
        int elementNum = 0;
        if (children.size() == 3) {
            if (isChar) {
                valueType = ValueType.CHAR;
                initialType = BaseType.CHAR8;
            } else {
                valueType = ValueType.INT;
                initialType = BaseType.INT32;
            }
        } else {
            elementNum = children.get(2).execute();
            if (isChar) {
                valueType = ValueType.CHAR_ARRAY;
                initialType = new ArrayType(BaseType.CHAR8, elementNum);
            } else {
                valueType = ValueType.INT_ARRAY;
                initialType = new ArrayType(BaseType.INT32, elementNum);
            }
        }
        ArrayList<Integer> values = ((ConstInitVal) children.get(children.size() - 1)).executeForConst(elementNum);
        initial = new Initial(initialType, values, isChar);
        ConstSymbol constSymbol = new ConstSymbol(symbolName, valueType);
        constSymbol.setInitial(initial);
        isChar = false;
        return constSymbol;
    } //虽然但是对这个initial的作用还不是太理解，先空着


    @Override
    public void checkError() {
        this.constSymbol = createSymbol();
        super.checkError();
        //b:标识符重定义
        if (!SymbolStack.getInstance().addSymbol(constSymbol)) {
            int errorLine = ((TokenNode) children.get(0)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
    }

    public Value genIR() {
        //由于符号表阶段使用的是一次性的符号表，所以这里要重新在构建一次符号表
        SymbolStack.getInstance().addSymbol(constSymbol);
        Initial init = constSymbol.getInitial();
        if (constSymbol.isGlobal()) {
            //此时的llvm_ir应该是一个指针
            String globalName = IRBuilder.getInstance().genGlobalVarName();
            GlobalVar globalVar = new GlobalVar(new PointerType(init.getLLVMType()), globalName, init);
            constSymbol.setLLVMValue(globalVar);
            //注意全局变量的话不用instr声明。直接一条语句就可以
        } else {
            //局部变量，先生成load指令，在生成store指令
            Instr instr;
            ValueType valueType = constSymbol.getValueType();
            if (valueType == ValueType.CHAR || valueType == ValueType.INT) {
                //非数组
                LLVMType allocType;
                if (valueType == ValueType.CHAR) {
                    allocType = BaseType.CHAR8;
                } else {
                    allocType = BaseType.INT32;
                } //allocType指的是要分配的空间的类型
                instr = new AllocaInstr(allocType, IRBuilder.getInstance().genLocalVarName());
                constSymbol.setLLVMValue(instr);
                int value = init.getValues().get(0);
                Value storeValue = new Constant(value);
                if (allocType == BaseType.CHAR8) {
                    storeValue.setType(BaseType.CHAR8);
                }
                new StoreInstr("", storeValue, instr);
            } else {
                //数组
                int elementNum = ((ArrayType) constSymbol.getInitial().getLLVMType()).getElementNum();
                LLVMType allocType;
                if (constSymbol.getValueType() == ValueType.CHAR_ARRAY) {
                    allocType = new ArrayType(BaseType.CHAR8, elementNum);
                } else {
                    allocType = new ArrayType(BaseType.INT32, elementNum);
                }
                instr = new AllocaInstr(allocType, IRBuilder.getInstance().genLocalVarName());
                constSymbol.setLLVMValue(instr);
                //gep计算指针偏移
                Value pointer = instr;
                int offset = 0;
                for (Integer value : init.getValues()) {
                    if (init.isChar()) {
                        instr = new GEPInstr(IRBuilder.getInstance().genLocalVarName(),
                                pointer, new Constant(offset), BaseType.CHAR8);
                    } else {
                        instr = new GEPInstr(IRBuilder.getInstance().genLocalVarName(),
                                pointer, new Constant(offset), BaseType.INT32);
                    }
                    PointerType pointerType = (PointerType) instr.getType();
                    Value storeValue = new Constant(value);
                    if (pointerType.getDestType() == BaseType.CHAR8) {
                        storeValue.setType(BaseType.CHAR8);
                    }
                    new StoreInstr("", storeValue, instr);
                    offset = offset + 1;
                }
            }
        }
        return null;
        //不太理解为什么这地方直接返回的是null而不是instr
        //已经是void了为啥还要生成名字？？
    }
}
