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

package org.panda_lang.panda.framework.design.interpreter.pattern.abyss;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.extractor.AbyssExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.mapping.AbyssPatternMappingHollows;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.List;

public class AbyssPatternUtils {

    public static @Nullable AbyssPatternMappingHollows extract(AbyssPattern pattern, SourceStream source) {
        AbyssExtractor extractor = pattern.extractor();
        TokenReader reader = source.toTokenReader();
        List<Tokens> gaps = extractor.extract(reader);

        if (gaps == null) {
            return null;
        }

        source.readDifference(reader);
        return new AbyssPatternMappingHollows(gaps);
    }

    public static boolean match(AbyssPattern pattern, TokenReader reader) {
        TokenReader copyOfReader = new PandaTokenReader(reader);
        AbyssExtractor extractor = pattern.extractor();

        List<Tokens> hollows = extractor.extract(copyOfReader);
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
