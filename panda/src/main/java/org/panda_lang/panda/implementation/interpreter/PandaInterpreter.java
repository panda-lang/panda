package org.panda_lang.panda.implementation.interpreter;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.composition.parser.ParserComposition;
import org.panda_lang.panda.implementation.runtime.PandaApplication;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParser;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserContext;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserInfo;
import org.panda_lang.panda.implementation.runtime.element.PandaWrapper;

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
            PandaLexer lexer = new PandaLexer(panda, sourceFile.getContent());

            TokenizedSource tokenizedSource = lexer.convert();
            TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

            ParserContext parserContext = new PandaParserContext(lexer.getSource(), tokenizedSource);
            parserContext.setTokenReader(tokenReader);
            parserInfo.setParserContext(parserContext);

            PandaParser pandaParser = new PandaParser(this);
            PandaWrapper wrapper = pandaParser.parse(parserInfo);

            PandaScript pandaScript = new PandaScript(wrapper.getName(), wrapper);
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
