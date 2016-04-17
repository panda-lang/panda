package org.panda_lang.panda.lang;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

import java.io.File;

public class PandaEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Panda");
        vial.group("panda.lang");
        vial.method(new Method("reload", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                alice.getPanda().reload();
                return null;
            }
        }));
        vial.method(new Method("loadSinglePandaFile", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                Essence essence = alice.getValueOfFactor(0);
                if (essence instanceof StringEssence) {
                    PandaScript pandaScript = alice.getPanda().getPandaLoader().loadSimpleScript(essence.toString());
                    alice.getPanda().addScript(pandaScript);
                }
                else if (essence instanceof FileEssence) {
                    File file = ((FileEssence) essence).getFile();
                    PandaScript pandaScript = alice.getPanda().getPandaLoader().loadSingleScript(file);
                    alice.getPanda().addScript(pandaScript);
                }
                return null;
            }
        }));
        vial.method(new Method("getVersion", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return new StringEssence(Panda.PANDA_VERSION);
            }
        }));
    }

}
