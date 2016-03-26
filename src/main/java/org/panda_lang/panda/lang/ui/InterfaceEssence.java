package org.panda_lang.panda.lang.ui;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.ObjectEssence;
import org.panda_lang.panda.lang.ui.util.PandaInterface;

public class InterfaceEssence extends ObjectEssence {

    private static final Vial vial;

    static {
        vial = new Vial("Interface");
        vial.group("panda.lang.ui");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                return new InterfaceEssence();
            }
        });
    }

    private final PandaInterface application;

    public InterfaceEssence() {
        this.application = new PandaInterface();
        this.application.run();
    }

    public PandaInterface getApplication() {
        return application;
    }

}
