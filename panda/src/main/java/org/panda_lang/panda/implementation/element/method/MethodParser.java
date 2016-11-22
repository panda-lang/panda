package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.parser.UnifiedParser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.util.Components;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.TokenUtils;
import org.panda_lang.core.interpreter.token.TokensSet;
import org.panda_lang.core.structure.WrapperLinker;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;

import java.util.List;

@ParserRegistration(parserClass = MethodParser.class, handlerClass = MethodParser.MethodParserHandler.class)
public class MethodParser implements UnifiedParser {

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
    public MethodWrapper parse(ParserInfo parserInfo) {
        ParserContext parserContext = parserInfo.getComponent(Components.PARSER_CONTEXT);
        TokenReader tokenReader = parserContext.getTokenReader();

        TokenExtractor extractor = pattern.extractor();
        extractor.extract(tokenReader);

        List<TokensSet> hollows = extractor.getHollows();
        TokensSet methodNameHollow = hollows.get(0);
        TokensSet parametersHollow = hollows.get(1);
        TokensSet bodyHollow = hollows.get(2);

        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.WRAPPER_LINKER);
        int id = wrapperLinker.reserveSlot();

        OverallParser overallParser = new OverallParser(parserInfo);

        Method method = Method.builder()
                .classPrototype(parserInfo.getComponent("class-prototype"))
                .methodName(TokenUtils.extractToken(methodNameHollow, 0))
                .methodBody(null)
                .isStatic(true)
                .visibility(MethodVisibility.PUBLIC)
                .build();

        MethodWrapper wrapper = new MethodWrapper(id, method);
        wrapperLinker.linkWrapper(id, wrapper);

        return wrapper;
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
