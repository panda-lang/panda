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

package org.panda_lang.panda.language.structure.main;

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.linker.WrapperLinker;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.reader.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;

import java.util.List;

//@ParserRegistration(parserClass = MainParser.class, handlerClass = MainParserHandler.class)
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

        TokenReader tokenReader = parserInfo.getComponent(Components.SOURCE_STREAM);
        Extractor extractor = pattern.extractor();
        List<TokenizedSource> gaps = extractor.extract(tokenReader);

        if (gaps == null) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        TokenizedSource body = gaps.get(0);
        TokenReader bodyReader = new PandaTokenReader(body);

        OverallParser overallParser = new OverallParser(parserInfo);
        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.LINKER);
        wrapperLinker.pushWrapper(main);

        for (Statement statement : overallParser) {
            main.addStatement(statement);
        }

        return main;
    }

}
