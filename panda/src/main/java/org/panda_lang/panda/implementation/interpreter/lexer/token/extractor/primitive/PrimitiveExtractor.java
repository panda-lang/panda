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

package org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.primitive;

import org.panda_lang.framework.interpreter.lexer.token.extractor.Extractor;
import org.panda_lang.framework.interpreter.lexer.token.reader.TokenReader;
import org.panda_lang.framework.interpreter.lexer.token.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.lexer.token.Token;
import org.panda_lang.framework.tool.array.ArrayDistributor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUnit;
import org.panda_lang.panda.implementation.interpreter.lexer.token.PandaTokenizedSource;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveExtractor implements Extractor {

    private final TokenPattern pattern;
    private final List<TokenizedSource> gaps;

    public PrimitiveExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    @Override
    public List<TokenizedSource> extract(TokenReader tokenReader) {
        tokenReader.synchronize();

        TokenPatternUnit[] units = pattern.getUnits();
        ArrayDistributor<TokenPatternUnit> unitsDistributor = new ArrayDistributor<>(units);
        PrimitiveOppositesKeeper extractorOpposites = new PrimitiveOppositesKeeper(this);
        TokenizedSource gap = new PandaTokenizedSource();

        for (int unitIndex = 0; unitIndex < units.length; unitIndex++) {
            TokenPatternUnit unit = unitsDistributor.get(unitIndex);
            tokenReader.synchronize();

            for (TokenRepresentation tokenRepresentation : tokenReader) {
                Token token = tokenRepresentation.getToken();

                if (unit.equals(token)) {
                    tokenReader.read();
                    break;
                }

                if (!unit.isGap()) {
                    return null;
                }

                extractorOpposites.report(token);
                gap.addToken(tokenRepresentation);
                tokenReader.read();
                tokenReader.synchronize();

                TokenPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

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
                gap = new PandaTokenizedSource();
                break;
            }
        }

        if (!(tokenReader.getIndex() + 1 >= tokenReader.getTokenizedSource().size())) {
            return null;
        }

        return gaps;
    }

    public TokenPattern getPattern() {
        return pattern;
    }

}
