package org.panda_lang.panda;

import org.panda_lang.core.work.Application;

import java.util.ArrayList;
import java.util.Collection;

public class PandaApplication implements Application {

    private final Collection<PandaScript> pandaScripts;

    public PandaApplication() {
        this.pandaScripts = new ArrayList<>();
    }

    @Override
    public void launch(String[] arguments) {
    }

    public void addPandaScript(PandaScript pandaScript) {
        pandaScripts.add(pandaScript);
    }

    public Collection<PandaScript> getPandaScripts() {
        return pandaScripts;
    }

}
