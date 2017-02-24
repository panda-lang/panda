/*
 * Copyright (c) 2015-2017 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.language.structure.prototype.structure.method.parser;

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPattern;

//@ParserRegistration(parserClass = MethodParser.class, handlerClass = MethodParserHandler.class)
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
    public void parse(ParserInfo parserInfo) {
        /*
        ParserContext parserContext = parserInfo.getComponent(Components.PARSER_CONTEXT);
        TokenReader tokenReader = parserContext.getTokenReader();

        TokenExtractor primitive = PATTERN.primitive();
        primitive.extract(tokenReader);


        List<TokensSet> hollows = primitive.getGaps();
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
    }

}
