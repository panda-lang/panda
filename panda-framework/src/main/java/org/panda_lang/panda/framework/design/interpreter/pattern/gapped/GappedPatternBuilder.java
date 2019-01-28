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
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.ArrayList;
import java.util.List;

public class GappedPatternBuilder {

    private final List<GappedPatternUnit> units;
    private Token fissureToken;
    private int maxNestingLevel;
    private boolean keepOpposites;
    private boolean lastIndexAlgorithm;

    public GappedPatternBuilder() {
        this.units = new ArrayList<>();
        this.fissureToken = Separators.SEMICOLON;
        this.keepOpposites = true;
    }

    public GappedPatternBuilder maxNestingLevel(int maxNestingLevel) {
        this.maxNestingLevel = maxNestingLevel;
        return this;
    }

    public GappedPatternBuilder keepOpposites(boolean keepOpposites) {
        this.keepOpposites = keepOpposites;
        return this;
    }

    public GappedPatternBuilder lastIndexAlgorithm(boolean lastIndexAlgorithm) {
        this.lastIndexAlgorithm = lastIndexAlgorithm;
        return this;
    }

    public GappedPatternBuilder fissureToken(Token fissureToken) {
        this.fissureToken = fissureToken;
        return this;
    }

    public GappedPatternBuilder unit(Token token) {
        return unit(token.getType(), token.getTokenValue(), false);
    }


    public GappedPatternBuilder unit(Token token, boolean optional) {
        return unit(token.getType(), token.getTokenValue(), optional);
    }

    public GappedPatternBuilder unit(TokenType type, String token) {
        return unit(type, token, false);
    }

    public GappedPatternBuilder unit(TokenType type, String token, boolean optional) {
        GappedPatternUnit unit = new GappedPatternUnit(type, token, optional);
        units.add(unit);
        return this;
    }

    public GappedPatternBuilder simpleHollow() {
        units.add(GappedPatternTokens.SIMPLE_ABYSS);
        return this;
    }

    public GappedPatternBuilder hollow() {
        units.add(GappedPatternTokens.ABYSS);
        return this;
    }

    public GappedPatternBuilder compile(Syntax syntax, String expression) {
        GappedPatternCompiler compiler = new GappedPatternCompiler(this, syntax);
        compiler.compile(expression);
        return this;
    }

    public GappedPattern build() {
        GappedPatternUnit[] unitsArray = new GappedPatternUnit[units.size()];
        units.toArray(unitsArray);

        return new GappedPattern(unitsArray, fissureToken, maxNestingLevel, keepOpposites, lastIndexAlgorithm);
    }

}
