package org.panda_lang.panda.lang.ui;

import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.ObjectEssence;

public class InterfaceEssence extends ObjectEssence {

    private static final Vial vial;

    static {
        vial = new Vial("Interface");
        vial.group("panda.lang.ui");
    }

    public InterfaceEssence() {
        super(vial);
    }

}
