package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserPipeline;
import org.panda_lang.core.work.element.Executable;
import org.panda_lang.core.work.element.Wrapper;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.work.element.PandaWrapper;

public class PandaParser implements Parser {

    private final PandaInterpreter interpreter;

    public PandaParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Wrapper parse(ParserInfo parserInfo) {
        Wrapper wrapper = new PandaWrapper();

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
            wrapper.addExecutable(executable);
        }

        return wrapper;
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
