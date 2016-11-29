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

package org.panda_lang.panda.implementation.element.main;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.linker.WrapperLinker;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;

import java.util.List;

@ParserRegistration(parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    protected static final TokenPattern pattern = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "main")
            .unit(TokenType.SEPARATOR, "{")
            .gap()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public Statement parse(ParserInfo parserInfo) {
        Main main = new Main();

        TokenReader tokenReader = parserInfo.getComponent(Components.READER);
        TokenExtractor extractor = pattern.extractor();
        boolean matched = extractor.extract(tokenReader);
        List<TokenizedSource> hollows = extractor.getGaps();

        if (!matched) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        TokenizedSource body = hollows.get(0);
        TokenReader bodyReader = new PandaTokenReader(body);

        OverallParser overallParser = new OverallParser(parserInfo, bodyReader);
        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.LINKER);
        wrapperLinker.pushWrapper(main);

        for (Statement statement : overallParser) {
            main.addStatement(statement);
        }

        return main;
    }

}
