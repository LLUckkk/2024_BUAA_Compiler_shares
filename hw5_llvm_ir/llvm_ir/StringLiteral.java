package llvm_ir;

import llvm_ir.type.BaseType;
import llvm_ir.type.PointerType;
import llvm_ir.type.ArrayType;

public class StringLiteral extends Value {
    private String value;

    public StringLiteral(String name, String value) {
        super(new PointerType(new ArrayType(BaseType.CHAR8, value.length() + 1)), name);
        this.value = value;
        if (IRBuilder.mode == IRBuilder.AUTO_INSERT_MODE) {
            IRBuilder.getInstance().addStringLiteral(this);
        }
    }

    public String toString() {
        while (value.contains("\n")) {
            value = value.replace("\n", "\\0A");
        }
        return name + " = constant " + ((PointerType) type).getDestType() + " c\"" + value + "\\00\"";
    }
}
