package org.panda_lang.panda;

public class Panda {

    private final PandaLoader pandaLoader;
    private final PandaComposition pandaComposition;

    public Panda() {
        this.pandaLoader = new PandaLoader();
        this.pandaComposition = new PandaComposition();
    }

    public PandaComposition getPandaComposition() {
        return pandaComposition;
    }

    public PandaLoader getPandaLoader() {
        return pandaLoader;
    }

}
