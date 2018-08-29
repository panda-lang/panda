/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor.AbyssExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.List;

public class AbyssPatternUtils {

    public static AbyssRedactorHollows extract(AbyssPattern pattern, SourceStream source) {
        AbyssExtractor extractor = pattern.extractor();
        TokenReader reader = source.toTokenReader();
        List<TokenizedSource> gaps = extractor.extract(reader);

        if (gaps == null) {
            throw new PandaParserException("Cannot parse source at line " + TokenUtils.getLine(source.toTokenizedSource()));
        }

        source.readDifference(reader);
        return new AbyssRedactorHollows(gaps);
    }

    public static boolean match(AbyssPattern pattern, TokenReader reader) {
        TokenReader copyOfReader = new PandaTokenReader(reader);
        AbyssExtractor extractor = pattern.extractor();

        List<TokenizedSource> hollows = extractor.extract(copyOfReader);
        return hollows != null && hollows.size() == pattern.getAmountOfHollows();
    }

    public static int countGaps(AbyssPatternUnit[] units) {
        int gaps = 0;

        for (AbyssPatternUnit unit : units) {
            if (unit.isAbyss()) {
                gaps++;
            }
        }

        return gaps;
    }

}
