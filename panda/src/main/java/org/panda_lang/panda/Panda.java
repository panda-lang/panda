package org.panda_lang.panda;

import org.panda_lang.panda.util.embed.PandaEngineFactoryConstants;

public class Panda {

    private final PandaLoader pandaLoader;
    private final PandaComposition pandaComposition;

    public Panda() {
        this.pandaLoader = new PandaLoader(this);
        this.pandaComposition = new PandaComposition();
    }

    public String getVersion() {
        return PandaEngineFactoryConstants.VERSION;
    }

    public PandaComposition getPandaComposition() {
        return pandaComposition;
    }

    public PandaLoader getPandaLoader() {
        return pandaLoader;
    }

}
