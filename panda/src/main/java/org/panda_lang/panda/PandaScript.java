package org.panda_lang.panda;

import org.panda_lang.panda.implementation.work.element.PandaWrapper;

public class PandaScript {

    private final String scriptName;
    private final PandaWrapper pandaWrapper;

    public PandaScript(String scriptName, PandaWrapper pandaWrapper) {
        this.scriptName = scriptName;
        this.pandaWrapper = pandaWrapper;
    }

    public PandaWrapper getPandaWrapper() {
        return pandaWrapper;
    }

    public String getScriptName() {
        return scriptName;
    }

}
