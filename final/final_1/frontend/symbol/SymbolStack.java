package frontend.symbol;

import utils.Printer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class SymbolStack { //单例模式
    private static final SymbolStack symbolStack = new SymbolStack();
    private Stack<SymbolTable> stack;
    private HashMap<String, Stack<SymbolTable>> symbolNameMap;
    private FuncSymbol lastFunc;
    private int loopCnt;
    private int scopeCnt;
    private int curScope;
    private boolean isInFunc;
    private boolean isGloabl;

    private SymbolStack() {
        stack = new Stack<>();
        //stack.push(new SymbolTable()); //预先放进一张新表
        symbolNameMap = new HashMap<>();
        lastFunc = null;
        loopCnt = 0;
        scopeCnt = 0;
        curScope = 0;
        isInFunc = false;
        isGloabl = false;
    }

    public static SymbolStack getInstance() {
        return symbolStack;
    }

    public void enterBlock() {
        if (isInFunc) {
            isInFunc = false;
            return;
        }
        SymbolTable temp = new SymbolTable();
        stack.push(temp);
        scopeCnt++;
        curScope = scopeCnt;
        temp.setId(curScope);
    }

    public void exitBlock() {
        SymbolTable topTable = stack.pop();
        HashSet<Symbol> symbols = topTable.getSymbolTable();
        for (Symbol symbol : symbols) {
            String name = symbol.getSymbolName();
            symbolNameMap.get(name).pop();
        }
        if (!stack.isEmpty()) {
            curScope = stack.peek().getId();
        }
    }

    public void enterFunc(FuncSymbol funcSymbol) {
        this.lastFunc = funcSymbol;
        enterBlock();
        isInFunc = true;
    }

    public void exitFunc() {
        this.lastFunc = null;
    }

    public void enterLoop() {
        loopCnt++;
    }

    public void exitLoop() {
        loopCnt--;
    }

    public boolean isInLoop() {
        return loopCnt > 0;
    }

    public boolean addSymbol(Symbol symbol) {
        //取出栈顶的符号表
        SymbolTable topTable = stack.peek();
        if (topTable.findSymbol(symbol.getSymbolName()) != null) {
            return false;
        } else {
            topTable.addSymbol(symbol);
            Printer.addPrintSymbol(symbol, curScope);
            //System.out.println("add symbol: " + symbol.getSymbolName() + " " + curScope);
        }
        //注意对符号名映射表的维护
        if (symbolNameMap.get(symbol.getSymbolName()) == null) {
            Stack<SymbolTable> stack = new Stack<>();
            stack.push(topTable);
            symbolNameMap.put(symbol.getSymbolName(), stack);
        } else {
            symbolNameMap.get(symbol.getSymbolName()).push(topTable);
        }
        return true;
    } //boolean表示是否添加成功

    public Symbol lookupSymbol(String name) {
        if (symbolNameMap.get(name) == null || symbolNameMap.get(name).isEmpty()) {
            return null;
        } else {
            return symbolNameMap.get(name).peek().findSymbol(name);
        }
    }

    public FuncSymbol getLastFunc() {
        return lastFunc;
    }

    public void setGlobal(boolean isGlobal){
        this.isGloabl = isGlobal;
    }

    public boolean isGlobal() {
        return isGloabl;
    }
}
