package org.panda_lang.panda.composition.parser;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenType;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.work.Executable;
import org.panda_lang.panda.composition.element.Method;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenHollow;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;

import java.util.List;

public class MethodParser implements Parser {

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

        List<TokenHollow> hollows = extractor.getHollows();

        TokenHollow methodNameHollow = hollows.get(0);
        TokenHollow parametersHollow = hollows.get(1);
        TokenHollow bodyHollow = hollows.get(2);

        Method method = new Method(methodNameHollow.getTokens().get(0).getToken());

        return method;
    }

    public static class MethodParserHandler implements ParserHandler {

        @Override
        public boolean handle(TokenReader tokenReader) {
            Token token = tokenReader.next();
            return !(token == null || token.getType() != TokenType.KEYWORD || !token.getToken().equals("method"));
        }

    }

}
