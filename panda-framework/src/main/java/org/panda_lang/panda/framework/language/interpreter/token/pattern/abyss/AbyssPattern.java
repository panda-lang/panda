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

package org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.prepared.PreparedExtractor;
import org.panda_lang.panda.framework.language.interpreter.token.reader.PandaTokenReader;

import java.util.List;

public class AbyssPattern {

    private final AbyssPatternUnit[] units;
    private final Token fissureToken;
    private final int amountOfHollows;
    private final boolean keepingOpposites;
    private final boolean lastIndexAlgorithm;

    public AbyssPattern(AbyssPatternUnit[] units, Token fissureToken, boolean keepOpposites, boolean lastIndexAlgorithm) {
        this.units = units;
        this.fissureToken = fissureToken;
        this.keepingOpposites = keepOpposites;
        this.lastIndexAlgorithm = lastIndexAlgorithm;
        this.amountOfHollows = AbyssPatternUtils.countGaps(units);
    }

    public List<TokenizedSource> match(TokenizedSource source) {
        return match(new PandaTokenReader(source));
    }

    public List<TokenizedSource> match(TokenReader tokenReader) {
        int index = tokenReader.getIndex();

        Extractor extractor = extractor();
        List<TokenizedSource> result = extractor.extract(tokenReader);

        tokenReader.setIndex(index);
        return result;
    }

    public Extractor extractor() {
        return new PreparedExtractor(this);
    }

    public boolean endsWithGap() {
        return units[units.length - 1].isGap();
    }

    public boolean hasLastIndexAlgorithmEnabled() {
        return lastIndexAlgorithm;
    }

    public boolean hasKeepingOppositesEnabled() {
        return keepingOpposites;
    }

    public int getAmountOfHollows() {
        return amountOfHollows;
    }

    public Token getFissureToken() {
        return fissureToken;
    }

    public AbyssPatternUnit[] getUnits() {
        return units;
    }

}
