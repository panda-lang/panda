package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;

import java.io.InputStream;

public class InputStreamInst extends ObjectInst {

    static {
        Structure structure = new Structure("InputStream");
        structure.group("panda.io");
    }

    private InputStream inputStream;

    public InputStreamInst() {
    }

}
