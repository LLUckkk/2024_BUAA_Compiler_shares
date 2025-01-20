package llvm_ir;

import java.util.HashMap;
import java.util.Stack;

public class LLVMManager {
    public static int AUTO_INSERT_MODE = 1;
    public static int DEFAULT_MODE = 0;
    public static int mode = DEFAULT_MODE;

    private static LLVMManager irBuilder = new LLVMManager();

    //变量命名部分
    private static String LOCAL_VAR_PREFIX = "%local_var_";
    private static String GLOBAL_VAR_PREFIX = "@global_var_";
    private static String BLOCK_NAME_PREFIX = "block_";
    private static String STRING_LITERAL_PREFIX = "@str_";
    private static String FUNCTION_NAME_PREFIX = "@func_";
    private static String PARAM_NAME_PREFIX = "%param_";
    //计数全局变量
    private int globalVarIndex;
    //计数基本块
    private int blockIndex;
    //计数字符串常量
    private int stringIndex;
    //计数param
    private int paramIndex;

    //存储每个函数由多少个局部变量
    private HashMap<Function, Integer> indexMap;
    //存储循环部分，break和continue需要用到
    private Stack<Loop> loopStack;

    //存储当前识别到哪个函数
    private Function curFunc;
    private Module curModule;
    private BasicBlock curBasicBlock;


    private LLVMManager() {
        indexMap = new HashMap<>();
        globalVarIndex = 0;
        blockIndex = 0;
        stringIndex = 0;
        paramIndex = 0;
        loopStack = new Stack<>();
        curBasicBlock = null;
        curFunc = null;
        curModule = new Module();
    }

    public static LLVMManager getInstance() {
        return irBuilder;
    }

    //生成局部变量名
    public String genLocalVarName() {
        int curIndex = indexMap.get(curFunc);
        String name = LOCAL_VAR_PREFIX + curIndex;
        indexMap.put(curFunc, curIndex + 1);
        return name;
    }

    //生成全局变量名
    public String genGlobalVarName() {
        String varName = GLOBAL_VAR_PREFIX + globalVarIndex;
        globalVarIndex++;
        return varName;
    }

    //生成block名
    public String genBlockName() {
        String blockName = BLOCK_NAME_PREFIX + blockIndex;
        blockIndex++;
        return blockName;
    }

    //生成字符串常量名
    public String genStringLiteralName() {
        String stringLiteral = STRING_LITERAL_PREFIX + stringIndex;
        stringIndex++;
        return stringLiteral;
    }

    //生成函数名
    public String genFunctionName(String name) {
        if (name.equals("main")) {
            return "@" + name;
        } else {
            return FUNCTION_NAME_PREFIX + name;
        }
    }

    //生成参数名
    public String genParamName() {
        String paramName = PARAM_NAME_PREFIX + paramIndex;
        paramIndex++;
        return paramName;
    }

    //ADD一些东西，暂时不知道原理
    public void addFunction(Function func) {
        curModule.addFunction(func);
    }

    public void addInstr(Instr instr) {
        curBasicBlock.addInstr(instr);
        instr.setParent(curBasicBlock);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        curModule.addGlobalVar(globalVar);
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        curFunc.addBasicBlock(basicBlock);
        basicBlock.setParentFunction(curFunc);
    }

    public void addStringLiteral(StringLiteral stringLiteral) {
        curModule.addStringLiteral(stringLiteral);
    }

    public void addParam(Param param) {
        curFunc.addParam(param);
        param.setBelong(curFunc);
    }

    public void pushLoop(Loop loop) {
        loopStack.push(loop);
    }

    public void popLoop() {
        loopStack.pop();
    }

    public Loop getCurLoop() {
        return loopStack.peek();
    }

    public void setCurBlock(BasicBlock basicBlock) {
        this.curBasicBlock = basicBlock;
    }

    public void setCurFunc(Function func) {
        indexMap.put(func, 0);
        curFunc = func;
    }

    public Module getModule() {
        return curModule;
    }
}
