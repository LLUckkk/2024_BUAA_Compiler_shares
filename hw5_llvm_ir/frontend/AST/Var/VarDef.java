package frontend.AST.Var;

import frontend.AST.Exp.ConstExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import frontend.symbol.SymbolStack;
import frontend.symbol.ValueType;
import frontend.symbol.VarSymbol;
import llvm_ir.instr.*;
import utils.Printer;
import llvm_ir.*;
import llvm_ir.initial.Initial;
import llvm_ir.type.ArrayType;
import llvm_ir.type.BaseType;
import llvm_ir.type.LLVMType;
import llvm_ir.type.PointerType;

import java.util.ArrayList;

public class VarDef extends Node {
    private boolean isChar;
    private VarSymbol varSymbol;

    //文法： VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '='InitVal
    public VarDef(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
        isChar = false;
    }

    public void setIsChar() {
        isChar = true;
    }

    public VarSymbol createVarSymbol() {
        String symbolName = ((TokenNode) children.get(0)).getToken().getValue();
        Initial initial = null;
        LLVMType llvmType = null;
        VarSymbol var;
        int elementNum = 0;
        if (children.size() == 1 || (children.size() == 3 &&
                ((TokenNode) children.get(1)).getToken().getValue().equals("="))) {
            if (isChar) {
                //isChar = false;
                llvmType = BaseType.CHAR8;
                var = new VarSymbol(symbolName, ValueType.CHAR);
            } else {
                llvmType = BaseType.INT32;
                var = new VarSymbol(symbolName, ValueType.INT);
            }
        } else {
            elementNum = children.get(2).execute(); //constExp计算
            if (isChar) {
                //isChar = false;
                llvmType = new ArrayType(BaseType.CHAR8, elementNum);
                var = new VarSymbol(symbolName, ValueType.CHAR_ARRAY);
            } else {
                llvmType = new ArrayType(BaseType.INT32, elementNum);
                var = new VarSymbol(symbolName, ValueType.INT_ARRAY);
            }
        }
        var.setLLVMType(llvmType);
        if (SymbolStack.getInstance().isGlobal()) {
            if (children.get(children.size() - 1) instanceof InitVal) {
                if (isChar) {
                    ArrayList<Integer> values =
                            ((InitVal) children.get(children.size() - 1)).executeForCharArray(elementNum);
                    initial = new Initial(llvmType, values, true);
                } else {
                    ArrayList<Integer> values =
                            ((InitVal) children.get(children.size() - 1)).executeForIntArray(elementNum);
                    initial = new Initial(llvmType, values, false);
                }
            } else {
                initial = new Initial(llvmType);
            }
        }
        var.setInitial(initial);
        isChar = false;
        return var;
    }

    public void checkError() {
        this.varSymbol = createVarSymbol();
        super.checkError();
        //b:当前作用域重复定义
        if (!SymbolStack.getInstance().addSymbol(varSymbol)) {
            int errorLine = ((TokenNode) children.get(0)).getToken().getLine();
            Printer.addError("b", errorLine);
        }
    }

    public Value genIR() {
        SymbolStack.getInstance().addSymbol(varSymbol);
        Initial initial = varSymbol.getInitial();
        //如果是全局变量，初始值已经计算出来了，
        if (varSymbol.isGlobal()) {
            String varName = IRBuilder.getInstance().genGlobalVarName();
            GlobalVar globalVar = new GlobalVar(new PointerType(initial.getLLVMType()), varName, initial);
            varSymbol.setLLVMVar(globalVar); //这里村的就是再var中的变量，比如@a类似
        } else {
            Instr instr;
            boolean isChar = varSymbol.isChar();
            //数组
            if (varSymbol.isArray()) {
                LLVMType allocType;
                int elementNum = ((ArrayType) varSymbol.getLLVMType()).getElementNum();
                if (varSymbol.isCharArray()) {
                    allocType = new ArrayType(BaseType.CHAR8, elementNum);
                } else {
                    allocType = new ArrayType(BaseType.INT32, elementNum);
                }
                instr = new AllocaInstr(allocType, IRBuilder.getInstance().genLocalVarName());
                varSymbol.setLLVMVar(instr); //相当于以后我要使用这个变量的话直接那这个instr就可以了
                //初始值存入
                if (children.get(children.size() - 1) instanceof InitVal initVal) { //说明有初始值
                    Value pointer = instr;
                    ArrayList<Value> valueList =
                            initVal.genIRValList(((ArrayType) varSymbol.getLLVMType()).getElementNum(),
                                    varSymbol.isCharArray()); //注意这里是value，之前那个是global才算的值，二者不一样
                    int offset = 0;
                    LLVMType elementType = varSymbol.isCharArray() ? BaseType.CHAR8 : BaseType.INT32;
                    for (Value value : valueList) {
                        instr = new GEPInstr(IRBuilder.getInstance().genLocalVarName(), pointer,
                                new Constant(offset), elementType);
                        PointerType pointerType = (PointerType) instr.getType();
                        if (value.getType() == BaseType.CHAR8 && pointerType.getDestType() == BaseType.INT32) {
                            value = new ZextInstr(IRBuilder.getInstance().genLocalVarName(), value, BaseType.INT32);
                        } else if (value.getType() == BaseType.INT32 && pointerType.getDestType() == BaseType.CHAR8) {
                            value = new TruncInstr(IRBuilder.getInstance().genLocalVarName(),
                                    BaseType.INT32, BaseType.CHAR8, value);
                        }
                        new StoreInstr("", value, instr);
                        offset++;
                    }
                }
            } else {
                LLVMType allocType;
                if (isChar) {
                    allocType = BaseType.CHAR8;
                } else {
                    allocType = BaseType.INT32;
                }
                instr = new AllocaInstr(allocType, IRBuilder.getInstance().genLocalVarName());
                varSymbol.setLLVMVar(instr);
                if (children.get(children.size() - 1) instanceof InitVal initVal) {
                    //说明有初始值
                    Value initValue = initVal.genIR();
                    PointerType pointerType = (PointerType) instr.getType();
                    if (initValue.getType() == BaseType.INT32 && pointerType.getDestType() == BaseType.CHAR8) {
                        initValue = new TruncInstr(IRBuilder.getInstance().genLocalVarName(),
                                BaseType.INT32, BaseType.CHAR8, initValue);
                    } else if (initValue.getType() == BaseType.CHAR8 && pointerType.getDestType() == BaseType.INT32) {
                        initValue = new ZextInstr(IRBuilder.getInstance().genLocalVarName(),
                                initValue, BaseType.INT32);
                    }
                    new StoreInstr("", initValue, instr);
                }
            }
        }
        return null;
    }
}
