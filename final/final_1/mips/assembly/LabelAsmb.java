package mips.assembly;

import mips.Assembly;

public class LabelAsmb extends Assembly {
    private String label;

    public LabelAsmb(String label){
        this.label = label;
    }

    public String toString(){
        return label + ":";
    }
}
