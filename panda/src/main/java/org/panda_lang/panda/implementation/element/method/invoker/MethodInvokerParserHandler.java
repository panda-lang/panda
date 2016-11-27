package org.panda_lang.panda.implementation.element.method.invoker;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;

public class MethodInvokerParserHandler implements ParserHandler {

    @Override
    public boolean handle(TokenReader tokenReader) {
        TokenReader copyOfTokenReader = new PandaTokenReader(tokenReader);
        TokenExtractor extractor = MethodInvokerParser.PATTERN.extractor();

        return extractor.extract(copyOfTokenReader);
    }

}
