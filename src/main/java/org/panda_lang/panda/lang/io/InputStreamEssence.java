package org.panda_lang.panda.lang.io;

import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.ObjectEssence;

import java.io.InputStream;

public class InputStreamEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("InputStream");
        vial.group("panda.io");
    }

    private InputStream inputStream;

    public InputStreamEssence() {
    }

}
