package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.Lexer;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.work.Executable;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.composition.ParserComposition;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;
import org.panda_lang.panda.lang.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.lang.interpreter.lexer.PandaTokenReader;

public class PandaParser implements Parser {

    private final PandaInterpreter interpreter;
    private PandaScript pandaScript;
    private TokenizedSource tokenizedSource;
    private TokenReader tokenReader;

    public PandaParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public PandaScript parse(ParserInfo parserInfo) {
        this.pandaScript = new PandaScript();

        ParserContext parserContext = parserInfo.getParserContext();
        Lexer lexer = new PandaLexer(interpreter.getPanda(), parserContext.getSource());

        this.tokenizedSource = lexer.convert();
        this.tokenReader = new PandaTokenReader(tokenizedSource);

        Panda panda = interpreter.getPanda();
        PandaComposition pandaComposition = panda.getPandaComposition();
        ParserComposition parserComposition = pandaComposition.getParserComposition();
        ParserPipeline parserPipeline = parserComposition.getPipeline();

        while (tokenReader.hasNext()) {
            Parser parser = parserPipeline.handle(tokenReader);

            if (parser == null) {
                throw new PandaParserException("Unrecognized syntax: " + tokenReader.toString());
            }

            Executable executable = parser.parse(parserInfo);
            pandaScript.addExecutable(executable);
        }

        return pandaScript;
    }

    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
