package org.panda_lang.panda.implementation.element.method.invoker;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;

import java.util.List;

@ParserRegistration(parserClass = MethodInvokerParser.class, handlerClass = MethodInvokerParserHandler.class, priority = 1)
public class MethodInvokerParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .gap()
            .unit(TokenType.SEPARATOR, ".")
            .gap()
            .unit(TokenType.SEPARATOR, "(")
            .gap()
            .unit(TokenType.SEPARATOR, ")")
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public Statement parse(ParserInfo parserInfo) {
        TokenReader reader = parserInfo.getComponent(Components.READER);

        TokenExtractor extractor = PATTERN.extractor();
        boolean matched = extractor.extract(reader);

        if (!matched) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        List<TokenizedSource> hollows = extractor.getGaps();
        TokenizedSource className = hollows.get(0);
        TokenizedSource concatenation = hollows.get(1);

        System.out.println(className + " | " + concatenation);

        return null;
    }

}
