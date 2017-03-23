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

package org.panda_lang.panda.implementation.interpreter.lexer.token.pattern;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.panda.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.panda.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.panda.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;

import java.util.List;

public class TokenPatternUtils {

    public static TokenPatternHollows extract(TokenPattern pattern, ParserInfo parserInfo) {
        SourceStream source = parserInfo.getComponent(Components.SOURCE_STREAM);

        Extractor extractor = pattern.extractor();
        TokenReader reader = source.toTokenReader();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Cannot parse group at line " + (source.read().getLine() + 1));
        }

        source.readDifference(reader);
        return new TokenPatternHollows(gaps);
    }

}
