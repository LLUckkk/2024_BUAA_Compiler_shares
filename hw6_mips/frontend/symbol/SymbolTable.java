package frontend.symbol;

import java.util.HashSet;

public class SymbolTable {
    private HashSet<Symbol> symbolTable;
    private int id;

    public SymbolTable() {
        symbolTable = new HashSet<Symbol>();
        id = 1;
    }

    public Symbol findSymbol(String name) {
        for (Symbol s : symbolTable) {
            if (s.getSymbolName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public void addSymbol(Symbol symbol) {
        symbolTable.add(symbol);
    }

    public HashSet<Symbol> getSymbolTable() {
        return symbolTable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
