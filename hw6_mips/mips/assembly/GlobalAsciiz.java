package mips.assembly;

public class GlobalAsciiz extends GlobalAsmb {
    private String content;

    public GlobalAsciiz(String name, String content) {
        super(name);
        this.content = content;
    }

    public String toString() {
        return name +
                ": .asciiz \"" +
                content.replace("\\0A", "\\n") +
                "\"\n";
    }
}
