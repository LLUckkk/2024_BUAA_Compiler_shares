package mips;

import mips.assembly.GlobalAsmb;

public class Assembly {
    public Assembly() {
        if (this instanceof GlobalAsmb) {
            MipsManager.getInstance().addDataAssembly(this);
        } else {
            MipsManager.getInstance().addTextAssembly(this);
        }
        //System.out.println(this);
    }

}
