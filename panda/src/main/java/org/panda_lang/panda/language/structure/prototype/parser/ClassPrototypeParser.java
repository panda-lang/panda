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

package org.panda_lang.panda.language.structure.prototype.parser;

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
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

import java.util.List;

@ParserRegistration(parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final TokenPattern pattern = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "class")
            .gap()
            .unit(TokenType.SEPARATOR, "{")
            .gap()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public Statement parse(ParserInfo parserInfo) {
        TokenReader tokenReader = parserInfo.getComponent(Components.READER);
        Extractor extractor = pattern.extractor();
        List<TokenizedSource> gaps = extractor.extract(tokenReader);

        if (gaps == null) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        ClassPrototype classPrototype = null;
        // TODO

        return new ClassPrototypeReference(classPrototype);
    }

}
