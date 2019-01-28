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

package org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.ArrayList;
import java.util.List;

public class GappedPatternExtractor {

    private final GappedPattern pattern;
    private final List<Tokens> gaps;

    public GappedPatternExtractor(GappedPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    public @Nullable List<Tokens> extract(Tokens source) {
        return extract(new PandaTokenReader(source));
    }

    public @Nullable List<Tokens> extract(TokenReader reader) {
        gaps.clear();

        GappedPatternUnit[] units = pattern.getUnits();
        Tokens tokens = reader.getTokenizedSource();
        GappedPatternExtractorSource source = new GappedPatternExtractorSource(tokens);

        int hardTypedUnits = GappedPatternExtractorSourceUtils.countHardTypedUnits(units);
        int[] positions = new int[hardTypedUnits];
        int[] indexes = new int[hardTypedUnits];
        boolean simpleAbyss = false;

        for (int i = 0, j = 0; i < units.length; i++) {
            GappedPatternUnit unit = units[i];

            if (unit.isOptional()) {
                continue;
            }

            if (unit.isAbyss()) {
                if (unit.isSimpleAbyss()) {
                    simpleAbyss = true;
                }

                continue;
            }

            int lastIndexOfUnit;

            if (!pattern.hasLastIndexAlgorithmEnabled() || (simpleAbyss && unit.equals(pattern.getFissureToken()))) {
                lastIndexOfUnit = GappedPatternExtractorSourceUtils.indexOf(source, unit, positions[j], pattern.getMaxNestingLevel(), simpleAbyss ? pattern.getFissureToken() : null);
            }
            else {
                lastIndexOfUnit = GappedPatternExtractorSourceUtils.lastIndexOf(source, unit, positions[j], pattern.getMaxNestingLevel(), simpleAbyss ? pattern.getFissureToken() : null);
            }

            if (lastIndexOfUnit == -1) {
                return null;
            }

            int index = j++;
            indexes[index] = i;
            positions[index] = lastIndexOfUnit;
            simpleAbyss = false;
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
            reader.synchronize();

            int currentIndex = indexes[i];
            int previousIndex = currentIndex - 1 < 0 ? 0 : currentIndex - 1;
            int indexOfUnit = positions[i] - 1;

            if (currentIndex > -1 && currentIndex < units.length) {
                GappedPatternUnit unit = units[previousIndex];

                if (!unit.isAbyss()) {
                    GappedPatternUnit currentUnit = units[currentIndex];
                    TokenRepresentation sourceToken = reader.read();

                    if (!sourceToken.contentEquals(currentUnit) && !unit.isOptional()) {
                        return null;
                    }

                    continue;
                }
            }

            Tokens gap = new PandaTokens();

            for (TokenRepresentation representation : reader) {
                int index = reader.getIndex();

                if (index >= indexOfUnit) {
                    break;
                }

                reader.read();
                gap.addToken(representation);
            }

            reader.read();
            gaps.add(gap);
        }

        //tokenReader.synchronize();

        /*
        if (pattern.endsWithSimpleGap()) {
            TokenizedSource lastGap = new PandaTokenizedSource();

            for (TokenRepresentation representation : tokenReader) {
                if (TokenType.KEYWORD.getTypeName().equals(representation.getTokenType())) {
                    break;
                }

                lastGap.addToken(representation);
                tokenReader.read();
            }

            gaps.add(lastGap);
        }
        else*/
        if (pattern.endsWithGap()) {
            Tokens lastGap;

            if (indexes.length > 0) {
                lastGap = tokens.subSource(positions[positions.length - 1] + 1, tokens.size());
            }
            else {
                lastGap = tokens.subSource(0, tokens.size());
            }

            gaps.add(lastGap);
        }

        //tokenReader.synchronize();
        return gaps;
    }

}
