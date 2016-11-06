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
    public Object getParameter(String s) {
        return null; // TODO
    }

    @Override
    public String getMethodCallSyntax(String instance, String method, String... parameters) {
        StringBuilder methodCallBuilder = new StringBuilder();
        methodCallBuilder.append(instance);
        methodCallBuilder.append(".");
        methodCallBuilder.append(method);
        methodCallBuilder.append("(");

        for (int i = 0; i < parameters.length; i++) {
            methodCallBuilder.append(parameters[i]);

            if (i + 1 < parameters.length) {
                methodCallBuilder.append(",");
            }
        }

        methodCallBuilder.append(")");
        return methodCallBuilder.toString();
    }

    @Override
    public String getOutputStatement(String s) {
        return null; // TODO
    }

    @Override
    public String getProgram(String... strings) {
        return null; // TODO
    }

    @Override
    public String getEngineName() {
        return PandaEngineFactoryConstants.ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return PandaEngineFactoryConstants.ENGINE_VERSION;
    }

    @Override
    public List<String> getExtensions() {
        return PandaEngineFactoryConstants.EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return PandaEngineFactoryConstants.MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
        return PandaEngineFactoryConstants.NAMES;
    }

    @Override
    public String getLanguageName() {
        return PandaEngineFactoryConstants.NAME;
    }

    @Override
    public String getLanguageVersion() {
        return PandaEngineFactoryConstants.VERSION;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return pandaEngine;
    }

}
