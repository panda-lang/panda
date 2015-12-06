package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class PNull extends Essence {

    static {
        vial = new Vial("null");
    }

    private static final Vial vial;

    public PNull() {
        super(vial);
    }

    @Override
    public String toString() {
        return "null";
    }

}
