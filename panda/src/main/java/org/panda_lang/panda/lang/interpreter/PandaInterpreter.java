package org.panda_lang.panda.lang.interpreter;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.Application;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.panda.lang.interpreter.util.PandaInterpreterConfiguration;

public class PandaInterpreter implements Interpreter {

    private final PandaInterpreterConfiguration pandaInterpreterConfiguration;
    private final SourceSet sourceSet;

    public PandaInterpreter(PandaInterpreterConfiguration pandaInterpreterConfiguration, SourceSet sourceSet) {
        this.pandaInterpreterConfiguration = pandaInterpreterConfiguration;
        this.sourceSet = sourceSet;
    }

    @Override
    public Application interpret() {
        return null;
    }

    public SourceSet getSourceSet() {
        return sourceSet;
    }

    public PandaInterpreterConfiguration getPandaInterpreterConfiguration() {
        return pandaInterpreterConfiguration;
    }

}
