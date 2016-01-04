package org.panda_lang.panda.lang;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaLoader;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PPanda extends PObject {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Panda");
        vial.group("panda.lang");
        vial.method(new Method("reload", new Executable() {
            @Override
            public Essence run(Particle particle) {
                Panda.getInstance().reload();
                return null;
            }
        }));
        vial.method(new Method("loadSimpleScript", new Executable() {
            @Override
            public Essence run(Particle particle) {
                Essence essence = particle.getValue(0);
                if (essence instanceof PString) {
                    PandaScript pandaScript = PandaLoader.loadSimpleScript(essence.toString());
                    Panda.getInstance().addScript(pandaScript);
                }
                return null;
            }
        }));
        vial.method(new Method("getVersion", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString(Panda.PANDA_VERSION);
            }
        }));
    }

}
