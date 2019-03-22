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

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.GappedPatternExtractor;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;

import java.util.List;

public class GappedPattern {

    private final GappedPatternUnit[] units;
    private final Token fissureToken;
    private final int amountOfHollows;
    private final int maxNestingLevel;
    private final boolean keepingOpposites;
    private final boolean lastIndexAlgorithm;

    public GappedPattern(GappedPatternUnit[] units, Token fissureToken, int maxNestingLevel, boolean keepOpposites, boolean lastIndexAlgorithm) {
        this.units = units;
        this.fissureToken = fissureToken;
        this.maxNestingLevel = maxNestingLevel;
        this.keepingOpposites = keepOpposites;
        this.lastIndexAlgorithm = lastIndexAlgorithm;
        this.amountOfHollows = GappedPatternUtils.countGaps(units);
    }

    public List<Snippet> match(Snippet source) {
        return match(new PandaTokenReader(source));
    }

    public List<Snippet> match(TokenReader tokenReader) {
        int index = tokenReader.getIndex();

        GappedPatternExtractor extractor = extractor();
        List<Snippet> result = extractor.extract(tokenReader);

        tokenReader.setIndex(index);
        return result;
    }

    public GappedPatternExtractor extractor() {
        return new GappedPatternExtractor(this);
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

    public GappedPatternUnit getLastUnit() {
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

    public GappedPatternUnit[] getUnits() {
        return units;
    }

}
