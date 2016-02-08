package org.panda_lang.panda.lang.ui;

import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class PInterface extends Essence {

    private static final Vial vial;

    static {
        vial = new Vial("Interface");
        vial.group("panda.lang.ui");
    }

    public PInterface() {
        super(vial);
    }

}
