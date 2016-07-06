package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.Panda;

import javax.script.ScriptEngineManager;

public class PandaEngineInitializer {

    public void initialize(Panda panda) {
        ScriptEngineManager m = new ScriptEngineManager();
        m.registerEngineName("panda", new PandaEngineFactory(panda));
    }

}
