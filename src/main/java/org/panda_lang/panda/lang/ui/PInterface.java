package org.panda_lang.panda.lang.ui;

import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Vial;

public class PInterface {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("Interface");
    }

}
