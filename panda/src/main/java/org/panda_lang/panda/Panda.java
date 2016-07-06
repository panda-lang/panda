package org.panda_lang.panda;

public class Panda {

    private final PandaLoader pandaLoader;

    public Panda() {
        this.pandaLoader = new PandaLoader();
    }

    public PandaLoader getPandaLoader() {
        return pandaLoader;
    }

}
