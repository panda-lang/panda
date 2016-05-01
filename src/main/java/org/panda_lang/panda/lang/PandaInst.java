package org.panda_lang.panda.lang;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

import java.io.File;

public class PandaInst extends ObjectInst {

    static {
        Structure structure = new Structure("Panda");
        structure.group("panda.lang");
        structure.method(new Method("reload", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                alice.getPanda().reload();
                return null;
            }
        }));
        structure.method(new Method("loadSinglePandaFile", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                Inst inst = alice.getValueOfFactor(0);
                if (inst instanceof StringInst) {
                    PandaScript pandaScript = alice.getPanda().getPandaLoader().loadSimpleScript(inst.toString());
                    alice.getPanda().addScript(pandaScript);
                }
                else if (inst instanceof FileInst) {
                    File file = ((FileInst) inst).getFile();
                    PandaScript pandaScript = alice.getPanda().getPandaLoader().loadSingleScript(file);
                    alice.getPanda().addScript(pandaScript);
                }
                return null;
            }
        }));
        structure.method(new Method("getVersion", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new StringInst(Panda.PANDA_VERSION);
            }
        }));
    }

}
