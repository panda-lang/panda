/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.GappedPatternExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping.GappedPatternMappingContent;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.List;

public class GappedPatternUtils {

    public static @Nullable GappedPatternMappingContent extract(GappedPattern pattern, SourceStream source) {
        GappedPatternExtractor extractor = pattern.extractor();
        TokenReader reader = source.toTokenReader();
        List<Snippet> gaps = extractor.extract(reader);

        if (gaps == null) {
            return null;
        }

        source.readDifference(reader);
        return new GappedPatternMappingContent(gaps);
    }

    public static boolean match(GappedPattern pattern, TokenReader reader) {
        TokenReader copyOfReader = new PandaTokenReader(reader);
        GappedPatternExtractor extractor = pattern.extractor();

        List<Snippet> hollows = extractor.extract(copyOfReader);
        return hollows != null && hollows.size() == pattern.getAmountOfHollows();
    }

    public static int countGaps(GappedPatternUnit[] units) {
        int gaps = 0;

        for (GappedPatternUnit unit : units) {
            if (unit.isAbyss()) {
                gaps++;
            }
        }

        return gaps;
    }

}
