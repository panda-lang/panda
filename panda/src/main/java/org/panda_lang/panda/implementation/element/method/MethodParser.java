package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.linker.WrapperLinker;
import org.panda_lang.core.interpreter.parser.util.Components;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.TokensSet;
import org.panda_lang.core.interpreter.token.util.TokenUtils;
import org.panda_lang.core.structure.Statement;
import org.panda_lang.panda.implementation.element.parameter.Parameter;
import org.panda_lang.panda.implementation.element.parameter.ParameterParser;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;

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

    public Method parse(ParserInfo parserInfo) {
        ParserContext parserContext = parserInfo.getComponent(Components.PARSER_CONTEXT);
        TokenReader tokenReader = parserContext.getTokenReader();

        TokenExtractor extractor = pattern.extractor();
        extractor.extract(tokenReader);

        List<TokensSet> hollows = extractor.getHollows();
        TokensSet methodNameHollow = hollows.get(0);
        TokensSet parametersHollow = hollows.get(1);
        TokensSet bodyHollow = hollows.get(2);

        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(parserInfo, parametersHollow);
        String methodName = TokenUtils.extractToken(methodNameHollow, 0);

        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.WRAPPER_LINKER);
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
    }

}
