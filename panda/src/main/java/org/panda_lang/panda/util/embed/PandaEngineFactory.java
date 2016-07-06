package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.Panda;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;

public class PandaEngineFactory implements ScriptEngineFactory {

    private final Panda panda;
    private final PandaEngine pandaEngine;

    public PandaEngineFactory(Panda panda) {
        this.panda = panda;
        this.pandaEngine = new PandaEngine(panda, this);
    }

    @Override
    public String getEngineName() {
        return null;
    }

    @Override
    public String getEngineVersion() {
        return null;
    }

    @Override
    public List<String> getExtensions() {
        return null;
    }

    @Override
    public List<String> getMimeTypes() {
        return null;
    }

    @Override
    public List<String> getNames() {
        return null;
    }

    @Override
    public String getLanguageName() {
        return null;
    }

    @Override
    public String getLanguageVersion() {
        return null;
    }

    @Override
    public Object getParameter(String s) {
        return null;
    }

    @Override
    public String getMethodCallSyntax(String s, String s1, String... strings) {
        return null;
    }

    @Override
    public String getOutputStatement(String s) {
        return null;
    }

    @Override
    public String getProgram(String... strings) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return pandaEngine;
    }

}
