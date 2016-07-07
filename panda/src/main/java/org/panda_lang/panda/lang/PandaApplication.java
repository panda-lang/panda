package org.panda_lang.panda.lang;

import org.panda_lang.core.Application;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserInitializer;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.lang.interpreter.parser.PandaParser;

import java.util.ArrayList;
import java.util.Collection;

public class PandaApplication implements Application {

    private final Collection<PandaScript> pandaScripts;
    private String workingDirectory;

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
