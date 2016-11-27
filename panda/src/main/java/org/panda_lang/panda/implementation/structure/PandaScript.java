package org.panda_lang.panda.implementation.structure;

import org.panda_lang.framework.interpreter.SourceFile;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.framework.structure.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class PandaScript implements Script {

    private final String scriptName;
    private final List<Wrapper> wrappers;

    public PandaScript(String scriptName) {
        this.scriptName = scriptName;
        this.wrappers = new ArrayList<>();
    }

    @Override
    public Wrapper select(Class<? extends Wrapper> clazz, String wrapperName) {
        for (Wrapper wrapper : wrappers) {
            if (!clazz.isInstance(wrapper)) {
                continue;
            }

            return wrapper;
        }

        return null;
    }

    public void addWrapper(Wrapper wrapper) {
        this.wrappers.add(wrapper);
    }

    public List<Wrapper> getWrappers() {
        return wrappers;
    }

    public String getScriptName() {
        return scriptName;
    }

    @Override
    public SourceFile getSourceFile() {
        return null;
    }

}
