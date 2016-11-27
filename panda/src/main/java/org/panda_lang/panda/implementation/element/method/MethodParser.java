package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;

@ParserRegistration(parserClass = MethodParser.class, handlerClass = MethodParserHandler.class)
public class MethodParser implements UnifiedParser {

    private static final TokenPattern pattern;

    static {
        pattern = TokenPattern.builder()
                .unit(TokenType.KEYWORD, "method")
                .gap()
                .unit(TokenType.SEPARATOR, "(")
                .gap()
                .unit(TokenType.SEPARATOR, ")")
                .unit(TokenType.SEPARATOR, "{")
                .gap()
                .unit(TokenType.SEPARATOR, "}")
                .build();
    }

    @Override
    public Method parse(ParserInfo parserInfo) {
        /*
        ParserContext parserContext = parserInfo.getComponent(Components.PARSER_CONTEXT);
        TokenReader tokenReader = parserContext.getTokenReader();

        TokenExtractor extractor = pattern.extractor();
        extractor.extract(tokenReader);


        List<TokensSet> hollows = extractor.getGaps();
        TokensSet methodNameHollow = hollows.get(0);
        TokensSet parametersHollow = hollows.get(1);
        TokensSet bodyHollow = hollows.get(2);

        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(parserInfo, parametersHollow);
        String methodName = TokenUtils.extractToken(methodNameHollow, 0);

        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.LINKER);
        int id = wrapperLinker.getNextID();

        MethodWrapper methodWrapper = new MethodWrapper(id, methodName, parameters);
        wrapperLinker.pushWrapper(methodWrapper);

        OverallParser overallParser = new OverallParser(parserInfo);
        for (Statement statement : overallParser) {
            methodWrapper.addStatement(statement);
        }

        // TODO

        Method method = Method.builder()
                .classPrototype(parserInfo.getComponent("class-prototype"))
                .methodName(TokenUtils.extractToken(methodNameHollow, 0))
                .methodBody(null)
                .isStatic(true)
                .visibility(MethodVisibility.PUBLIC)
                .build();

        return method;
        */

        return null;
    }

}
