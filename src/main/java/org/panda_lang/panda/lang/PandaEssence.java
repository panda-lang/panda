package org.panda_lang.panda.lang;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

import java.io.File;

public class PandaEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Panda");
        vial.group("panda.lang");
        vial.method(new Method("reload", new Executable() {
            @Override
            public Essence run(Particle particle) {
                particle.getPanda().reload();
                return null;
            }
        }));
        vial.method(new Method("loadSinglePandaFile", new Executable() {
            @Override
            public Essence run(Particle particle) {
                Essence essence = particle.getValueOfFactor(0);
                if (essence instanceof StringEssence) {
                    PandaScript pandaScript = particle.getPanda().getPandaLoader().loadSimpleScript(essence.toString());
                    particle.getPanda().addScript(pandaScript);
                }
                else if (essence instanceof FileEssence) {
                    File file = ((FileEssence) essence).getFile();
                    PandaScript pandaScript = particle.getPanda().getPandaLoader().loadSingleScript(file);
                    particle.getPanda().addScript(pandaScript);
                }
                return null;
            }
        }));
        vial.method(new Method("getVersion", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new StringEssence(Panda.PANDA_VERSION);
            }
        }));
    }

}
