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

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor.AbyssExtractor;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.List;

public class AbyssPattern {

    private final AbyssPatternUnit[] units;
    private final Token fissureToken;
    private final int amountOfHollows;
    private final int maxNestingLevel;
    private final boolean keepingOpposites;
    private final boolean lastIndexAlgorithm;

    public AbyssPattern(AbyssPatternUnit[] units, Token fissureToken, int maxNestingLevel, boolean keepOpposites, boolean lastIndexAlgorithm) {
        this.units = units;
        this.fissureToken = fissureToken;
        this.maxNestingLevel = maxNestingLevel;
        this.keepingOpposites = keepOpposites;
        this.lastIndexAlgorithm = lastIndexAlgorithm;
        this.amountOfHollows = AbyssPatternUtils.countGaps(units);
    }

    public List<TokenizedSource> match(TokenizedSource source) {
        return match(new PandaTokenReader(source));
    }

    public List<TokenizedSource> match(TokenReader tokenReader) {
        int index = tokenReader.getIndex();

        AbyssExtractor extractor = extractor();
        List<TokenizedSource> result = extractor.extract(tokenReader);

        tokenReader.setIndex(index);
        return result;
    }

    public AbyssExtractor extractor() {
        return new AbyssExtractor(this);
    }

    public boolean endsWithGap() {
        return getLastUnit().isAbyss();
    }

    public boolean endsWithSimpleGap() {
        return getLastUnit().isSimpleAbyss();
    }

    public boolean hasLastIndexAlgorithmEnabled() {
        return lastIndexAlgorithm;
    }

    public boolean hasKeepingOppositesEnabled() {
        return keepingOpposites;
    }

    public AbyssPatternUnit getLastUnit() {
        return units[units.length - 1];
    }

    public int getMaxNestingLevel() {
        return maxNestingLevel;
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
