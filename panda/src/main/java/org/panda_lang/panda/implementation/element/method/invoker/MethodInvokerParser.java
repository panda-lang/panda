/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.element.method.invoker;

import org.panda_lang.framework.interpreter.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.extractor.TokenPattern;
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

        Extractor extractor = PATTERN.extractor();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        TokenizedSource className = gaps.get(0);
        TokenizedSource concatenation = gaps.get(1);

        System.out.println(className + " | " + concatenation);

        return null;
    }

}
