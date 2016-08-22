package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserPipeline;
import org.panda_lang.core.work.Executable;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;

public class PandaParser implements Parser {

    private final PandaInterpreter interpreter;
    private PandaScript pandaScript;

    public PandaParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public PandaScript parse(ParserInfo parserInfo) {
        this.pandaScript = new PandaScript();

        ParserPipeline pipeline = parserInfo.getParserPipeline();
        ParserContext parserContext = parserInfo.getParserContext();
        TokenReader tokenReader = parserContext.getTokenReader();

        while (tokenReader.hasNext()) {
            tokenReader.synchronize();

            Parser parser = pipeline.handle(tokenReader);

            if (parser == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Unrecognized " + tokenReader.next());
            }

            Executable executable = parser.parse(parserInfo);
            pandaScript.addExecutable(executable);
        }

        return pandaScript;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
