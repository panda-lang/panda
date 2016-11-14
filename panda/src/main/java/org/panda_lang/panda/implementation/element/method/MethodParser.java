package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.parser.MatchedParser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.TokensSet;
import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.panda.composition.parser.ParserRegistration;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;

import java.util.List;

@ParserRegistration(parserClass = MethodParser.class, handlerClass = MethodParser.MethodParserHandler.class)
public class MethodParser implements MatchedParser {

    private static final TokenPattern pattern;

    static {
        pattern = TokenPattern.builder()
                .unit(TokenType.KEYWORD, "method")
                .hollow()
                .unit(TokenType.SEPARATOR, "(")
                .hollow()
                .unit(TokenType.SEPARATOR, ")")
                .unit(TokenType.SEPARATOR, "{")
                .hollow()
                .unit(TokenType.SEPARATOR, "}")
                .build();
    }

    @Override
    public Executable parse(ParserInfo parserInfo) {
        ParserContext parserContext = parserInfo.getParserContext();
        TokenReader tokenReader = parserContext.getTokenReader();

        TokenExtractor extractor = pattern.extractor();
        extractor.extract(tokenReader);

        List<TokensSet> hollows = extractor.getHollows();
        TokensSet methodNameHollow = hollows.get(0);
        TokensSet parametersHollow = hollows.get(1);
        TokensSet bodyHollow = hollows.get(2);

        // MethodWrapper method = new MethodWrapper(TokenUtils.get(methodNameHollow, 0));

        return null;
    }

    public static class MethodParserHandler implements ParserHandler {

        @Override
        public boolean handle(TokenReader tokenReader) {
            TokenRepresentation tokenRepresentation = tokenReader.next();
            Token token = tokenRepresentation.getToken();

            return !(token == null || token.getType() != TokenType.KEYWORD || !token.getToken().equals("method"));
        }

    }

}
