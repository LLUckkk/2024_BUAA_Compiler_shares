package mips;

import java.util.ArrayList;

public class AssemblyTable {
    ArrayList<Assembly> dataPart;
    ArrayList<Assembly> textPart;

    public AssemblyTable() {
        this.dataPart = new ArrayList<>();
        this.textPart = new ArrayList<>();
    }

    public void addDataAssembly(Assembly assembly) {
        this.dataPart.add(assembly);
    }

    public void addTextAssembly(Assembly assembly) {
        this.textPart.add(assembly);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        for (Assembly data : dataPart) {
            sb.append(data.toString());
            sb.append("\n");
        }
        sb.append("\n\n\n");
        sb.append(".text\n");
        for (Assembly assembly : textPart) {
            sb.append(assembly.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
