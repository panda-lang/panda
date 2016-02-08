package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

import java.io.InputStream;

public class PInputStream extends PObject {

    static {
        Vial vial = new Vial("InputStream");
        vial.group("panda.io");
    }

    private InputStream inputStream;

    public PInputStream() {
    }

}
