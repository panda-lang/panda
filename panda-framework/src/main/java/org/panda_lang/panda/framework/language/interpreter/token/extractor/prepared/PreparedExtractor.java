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

package org.panda_lang.panda.framework.language.interpreter.token.extractor.prepared;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPatternUnit;

import java.util.ArrayList;
import java.util.List;

public class PreparedExtractor implements Extractor {

    private final AbyssPattern pattern;
    private final List<TokenizedSource> gaps;

    public PreparedExtractor(AbyssPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    @Override
    public List<TokenizedSource> extract(TokenReader tokenReader) {
        gaps.clear();

        AbyssPatternUnit[] units = pattern.getUnits();
        TokenizedSource tokenizedSource = tokenReader.getTokenizedSource();
        PreparedSource preparedSource = new PreparedSource(tokenizedSource);

        int hardTypedUnits = PreparedSourceUtils.countHardTypedUnits(units);
        int[] positions = new int[hardTypedUnits];
        int[] indexes = new int[hardTypedUnits];
        boolean fissure = false;

        for (int i = 0, j = 0; i < units.length; i++) {
            AbyssPatternUnit unit = units[i];

            if (unit.isGap()) {
                if (unit.isFissure()) {
                    fissure = true;
                }

                continue;
            }

            int lastIndexOfUnit;

            if (!pattern.hasLastIndexAlgorithmEnabled() || (fissure && TokenUtils.equals(unit, pattern.getFissureToken()))) {
                lastIndexOfUnit = PreparedSourceUtils.indexOf(preparedSource, unit, positions[j], fissure ? pattern.getFissureToken() : null);
            }
            else {
                lastIndexOfUnit = PreparedSourceUtils.lastIndexOf(preparedSource, unit, positions[j], fissure ? pattern.getFissureToken() : null);
            }

            if (lastIndexOfUnit == -1) {
                return null;
            }

            int index = j++;
            indexes[index] = i;
            positions[index] = lastIndexOfUnit;
            fissure = false;
        }

        for (int i = 0, previousIndex = -1; i < positions.length; i++) {
            int index = positions[i];

            if (index > previousIndex) {
                previousIndex = index;
                continue;
            }

            return null;
        }

        for (int i = 0; i < positions.length; i++) {
            tokenReader.synchronize();

            int currentIndex = indexes[i];
            int previousIndex = currentIndex - 1 < 0 ? 0 : currentIndex - 1;
            int indexOfUnit = positions[i] - 1;

            if (currentIndex > -1 && currentIndex < units.length) {
                AbyssPatternUnit unit = units[previousIndex];

                if (!unit.isGap()) {
                    AbyssPatternUnit currentUnit = units[currentIndex];
                    TokenRepresentation sourceToken = tokenReader.read();

                    if (!TokenUtils.equals(sourceToken, currentUnit)) {
                        return null;
                    }

                    continue;
                }
            }

            TokenizedSource gap = new PandaTokenizedSource();

            for (TokenRepresentation representation : tokenReader) {
                int index = tokenReader.getIndex();

                if (index >= indexOfUnit) {
                    break;
                }

                tokenReader.read();
                gap.addToken(representation);
            }

            tokenReader.read();
            gaps.add(gap);
        }

        if (pattern.endsWithGap()) {
            TokenizedSource lastGap;

            if (indexes.length > 0) {
                lastGap = tokenizedSource.subSource(positions[positions.length - 1] + 1, tokenizedSource.size());
            }
            else {
                lastGap = tokenizedSource.subSource(0, tokenizedSource.size());
            }

            gaps.add(lastGap);
        }

        return gaps;
    }

}
