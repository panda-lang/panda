package org.panda_lang.panda;

import org.panda_lang.panda.core.PandaCore;

public class Panda {

    private final PandaCore pandaCore;
    private final PandaLoader pandaLoader;

    public Panda() {
        this.pandaCore = new PandaCore();
        this.pandaLoader = new PandaLoader();
    }

    public PandaLoader getPandaLoader() {
        return pandaLoader;
    }

    public PandaCore getPandaCore() {
        return pandaCore;
    }

}
