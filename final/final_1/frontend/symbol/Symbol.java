package frontend.symbol;

public class Symbol {
    private String name;
    private int scopeId;

    public Symbol(String name) {
        this.name = name;
    }

    public String getSymbolName() {
        return name;
    }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }

    public int getScopeId() {
        return scopeId;
    }
}
