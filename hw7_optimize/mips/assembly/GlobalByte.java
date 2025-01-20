package mips.assembly;

import java.util.ArrayList;

public class GlobalByte extends GlobalAsmb {
    private int value;
    private ArrayList<Integer> values;
    private int size;
    private boolean isArray;

    public GlobalByte(String name, int value) {
        super(name);
        this.value = value;
        size = 1;
        isArray = false;
    }

    public GlobalByte(String name, ArrayList<Integer> values) {
        super(name);
        this.values = values;
        size = values.size();
        isArray = true;
    }

    public GlobalByte(String name, int size, boolean isArray) {
        super(name);
        this.size = size;
        this.isArray = isArray;
        values = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(": .byte ");
        if (isArray) {
            if (values == null) {
                sb.append("0:");
                sb.append(size);
            } else {
                for (int i = 0; i < size; i++) {
                    sb.append(values.get(i));
                    if (i != size - 1) {
                        sb.append(", ");
                    }
                }
            }
        } else {
            sb.append(value);
        }
        sb.append("\n");
        return sb.toString();
    }
}
