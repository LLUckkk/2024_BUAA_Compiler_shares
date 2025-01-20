package mips.assembly;

import mips.Assembly;

public class CommentAsmb extends Assembly {
    private String comment;

    public CommentAsmb(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return comment;
    }
}
