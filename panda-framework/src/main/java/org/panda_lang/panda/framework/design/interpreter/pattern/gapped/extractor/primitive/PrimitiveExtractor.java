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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.primitive;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.utilities.commons.iterable.ArrayDistributor;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PrimitiveExtractor {

    private final GappedPattern pattern;
    private final List<Snippet> gaps;

    public PrimitiveExtractor(GappedPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    public List<Snippet> extract(TokenReader tokenReader) {
        tokenReader.synchronize();

        GappedPatternUnit[] units = pattern.getUnits();
        ArrayDistributor<GappedPatternUnit> unitsDistributor = new ArrayDistributor<>(units);
        PrimitiveOppositesKeeper extractorOpposites = new PrimitiveOppositesKeeper(this);
        Snippet gap = new PandaSnippet();

        for (int unitIndex = 0; unitIndex < units.length; unitIndex++) {
            GappedPatternUnit unit = unitsDistributor.get(unitIndex);
            tokenReader.synchronize();

            if (unit == null) {
                return null;
            }

            // TODO: Statement does not loop
            //noinspection LoopStatementThatDoesntLoop
            for (TokenRepresentation tokenRepresentation : tokenReader) {
                Token token = tokenRepresentation.getToken();

                if (unit.equals(token)) {
                    tokenReader.read();
                    break;
                }

                if (!unit.isAbyss()) {
                    return null;
                }

                extractorOpposites.report(token);
                gap.addToken(tokenRepresentation);
                tokenReader.read();
                tokenReader.synchronize();

                GappedPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

                for (TokenRepresentation gapRepresentation : tokenReader) {
                    Token gapToken = gapRepresentation.getToken();

                    if (!gapToken.equals(nextUnit) || extractorOpposites.isLocked()) {
                        extractorOpposites.report(gapToken);
                        tokenReader.read();
                        tokenReader.synchronize();
                        gap.addToken(gapRepresentation);
                        continue;
                    }

                    break;
                }

                gaps.add(gap);
                gap = new PandaSnippet();
                break;
            }
        }

        if (!(tokenReader.getIndex() + 1 >= tokenReader.getTokenizedSource().size())) {
            return null;
        }

        return gaps;
    }

    public GappedPattern getPattern() {
        return pattern;
    }

}
