package org.panda_lang.panda;

import java.util.ArrayList;
import java.util.Collection;

public class PandaApplication {

    private final Collection<PandaScript> pandaScripts;

    public PandaApplication() {
        this.pandaScripts = new ArrayList<>();
    }

    public void launch(String[] arguments) {
    }

    public void addPandaScript(PandaScript pandaScript) {
        pandaScripts.add(pandaScript);
    }

    public Collection<PandaScript> getPandaScripts() {
        return pandaScripts;
    }

}
