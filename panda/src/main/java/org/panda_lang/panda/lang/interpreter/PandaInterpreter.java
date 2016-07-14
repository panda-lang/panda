package org.panda_lang.panda.lang.interpreter;

import org.panda_lang.core.Application;
import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.lang.PandaApplication;
import org.panda_lang.panda.lang.interpreter.parser.PandaParser;
import org.panda_lang.panda.lang.interpreter.util.PandaInterpreterConfiguration;

public class PandaInterpreter implements Interpreter {

    private final PandaInterpreterConfiguration pandaInterpreterConfiguration;
    private final SourceSet sourceSet;

    public PandaInterpreter(PandaInterpreterConfiguration pandaInterpreterConfiguration, SourceSet sourceSet) {
        this.pandaInterpreterConfiguration = pandaInterpreterConfiguration;
        this.sourceSet = sourceSet;
    }

    @Override
    public PandaApplication interpret() {
        PandaApplication pandaApplication = new PandaApplication();

        for (SourceFile sourceFile : sourceSet.getSourceFiles()) {
            ParserInfo parserInfo = new ParserInfo(pandaApplication);
            parserInfo.setSource(sourceFile.getContent());

            PandaParser pandaParser = new PandaParser(this);
            PandaScript pandaScript = pandaParser.parse(parserInfo);

            pandaApplication.addPandaScript(pandaScript);
        }

        return pandaApplication;
    }

    public SourceSet getSourceSet() {
        return sourceSet;
    }

    public PandaInterpreterConfiguration getPandaInterpreterConfiguration() {
        return pandaInterpreterConfiguration;
    }

}
