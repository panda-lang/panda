package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.lexer.Lexer;
import org.panda_lang.core.interpreter.parser.lexer.TokenizedSource;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;
import org.panda_lang.panda.lang.interpreter.lexer.PandaLexer;

public class PandaParser implements Parser {

    private final PandaInterpreter interpreter;
    private PandaScript pandaScript;
    private TokenizedSource tokenizedSource;

    public PandaParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public PandaScript parse(ParserInfo parserInfo) {
        this.pandaScript = new PandaScript();

        ParserContext parserContext = parserInfo.getParserContext();
        Lexer lexer = new PandaLexer(interpreter.getPanda(), parserContext.getSource());

        this.tokenizedSource = lexer.convert();


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
