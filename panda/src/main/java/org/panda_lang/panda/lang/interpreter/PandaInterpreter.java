package org.panda_lang.panda.lang.interpreter;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.composition.ParserComposition;
import org.panda_lang.panda.lang.PandaApplication;
import org.panda_lang.panda.lang.interpreter.parser.PandaParser;
import org.panda_lang.panda.lang.interpreter.parser.PandaParserContext;
import org.panda_lang.panda.lang.interpreter.parser.PandaParserInfo;

public class PandaInterpreter implements Interpreter {

    private final Panda panda;
    private final SourceSet sourceSet;
    private final PandaApplication application;

    public PandaInterpreter(Panda panda, SourceSet sourceSet) {
        this.panda = panda;
        this.sourceSet = sourceSet;
        this.application = new PandaApplication();
    }

    @Override
    public void interpret() {
        PandaComposition pandaComposition = panda.getPandaComposition();
        ParserComposition parserComposition = pandaComposition.getParserComposition();

        ParserInfo parserInfo = new PandaParserInfo(this, parserComposition.getPipeline());

        for (SourceFile sourceFile : sourceSet.getSourceFiles()) {
            ParserContext parserContext = new PandaParserContext(sourceFile.getContent());
            parserInfo.setParserContext(parserContext);

            PandaParser pandaParser = new PandaParser(this);
            PandaScript pandaScript = pandaParser.parse(parserInfo);

            application.addPandaScript(pandaScript);
        }
    }

    @Override
    public PandaApplication getApplication() {
        return application;
    }

    public SourceSet getSourceSet() {
        return sourceSet;
    }

    public Panda getPanda() {
        return panda;
    }

}
