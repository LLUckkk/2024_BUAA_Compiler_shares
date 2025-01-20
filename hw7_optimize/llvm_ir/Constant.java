package llvm_ir;

import llvm_ir.type.BaseType;

public class Constant extends Value {
    private int value;

    public Constant(int value) {
        super(BaseType.INT32, String.valueOf(value));
        this.value = value;
    }

    public Constant(String value) {
        super(BaseType.CHAR8, value);
        if (value.length() > 3) {
            if (value.charAt(2) == 'a') {
                this.value = 7;
            } else if (value.charAt(2) == 'b') {
                this.value = 8;
            } else if (value.charAt(2) == 't') {
                this.value = 9;
            } else if (value.charAt(2) == 'n') {
                this.value = 10;
            } else if (value.charAt(2) == 'v') {
                this.value = 11;
            } else if (value.charAt(2) == 'f') {
                this.value = 12;
            } else if (value.charAt(2) == '\"') {
                this.value = 34;
            } else if (value.charAt(2) == '\'') {
                this.value = 39;
            } else if (value.charAt(2) == '\\') {
                this.value = 92;
            } else if (value.charAt(2) == '0') {
                this.value = 0;
            }
        } else {
            this.value = value.charAt(1);
        }
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return String.valueOf(value);
    }
}
