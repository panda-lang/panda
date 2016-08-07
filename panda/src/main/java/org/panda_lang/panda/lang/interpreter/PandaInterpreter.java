package org.panda_lang.panda.lang.interpreter;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.lang.PandaApplication;
import org.panda_lang.panda.lang.interpreter.parser.PandaParser;
import org.panda_lang.panda.lang.interpreter.parser.PandaParserContext;
import org.panda_lang.panda.lang.interpreter.parser.PandaParserInfo;

public class PandaInterpreter implements Interpreter {

    private final SourceSet sourceSet;

    public PandaInterpreter(SourceSet sourceSet) {
        this.sourceSet = sourceSet;
    }

    @Override
    public PandaApplication interpret() {
        PandaApplication pandaApplication = new PandaApplication();

        for (SourceFile sourceFile : sourceSet.getSourceFiles()) {
            ParserInfo parserInfo = new PandaParserInfo(pandaApplication);
            ParserContext parserContext = new PandaParserContext(sourceFile.getContent());
            parserInfo.setParserContext(parserContext);

            PandaParser pandaParser = new PandaParser(this);
            PandaScript pandaScript = pandaParser.parse(parserInfo);

            pandaApplication.addPandaScript(pandaScript);
        }

        return pandaApplication;
    }

    public SourceSet getSourceSet() {
        return sourceSet;
    }

}
