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

package org.panda_lang.panda.implementation.interpreter.lexer.prepared;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenizedSource;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPatternUnit;

import java.util.ArrayList;
import java.util.List;

public class PreparedExtractor {

    public static final TokenPatternUnit GAP = new TokenPatternUnit(new TokenType("GAP"), "*");

    private final TokenPattern pattern;
    private final List<TokenizedSource> gaps;

    public PreparedExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    public boolean extract(TokenReader tokenReader) {
        TokenPatternUnit[] units = pattern.getUnits();
        TokenizedSource tokenizedSource = tokenReader.getTokenizedSource();
        PreparedSource preparedSource = new PreparedSource(tokenizedSource);

        int hardTypedUnits = PreparedSourceUtils.countHardTypedUnits(units);
        int[] positions = new int[hardTypedUnits];

        for (int i = 0, j = 0; i < units.length; i++) {
            TokenPatternUnit unit = units[i];

            if (unit.isGap()) {
                continue;
            }

            int lastIndexOfUnit = PreparedSourceUtils.lastIndexOf(preparedSource, unit);

            if (lastIndexOfUnit == -1) {
                return false;
            }

            positions[j++] = lastIndexOfUnit;
        }

        int previousIndex = 0;

        for (int i = 0; i < positions.length; i++) {
            int index = positions[i];

            if (index > previousIndex) {
                previousIndex = index;
                continue;
            }

            return false;
        }

        for (int i = 0; i < positions.length; i++) {
            tokenReader.synchronize();

            TokenizedSource gap = new PandaTokenizedSource();
            int indexOfUnit = positions[i] - 1;

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

        return !tokenReader.hasNext();
    }



    protected TokenPattern getPattern() {
        return pattern;
    }

    public List<TokenizedSource> getGaps() {
        return gaps;
    }

}
