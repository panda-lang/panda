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

package org.panda_lang.panda.design.interpreter.token;

import org.panda_lang.panda.framework.design.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPatternTokens;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPatternUnit;
import org.panda_lang.panda.language.syntax.tokens.Separators;

import java.util.ArrayList;
import java.util.List;

public class AbyssPatternBuilder {

    private final List<AbyssPatternUnit> units;
    private Token fissureToken;
    private int maxNestingLevel;
    private boolean keepOpposites;
    private boolean lastIndexAlgorithm;

    public AbyssPatternBuilder() {
        this.units = new ArrayList<>();
        this.fissureToken = Separators.SEMICOLON;
        this.keepOpposites = true;
    }

    public AbyssPatternBuilder maxNestingLevel(int maxNestingLevel) {
        this.maxNestingLevel = maxNestingLevel;
        return this;
    }

    public AbyssPatternBuilder keepOpposites(boolean keepOpposites) {
        this.keepOpposites = keepOpposites;
        return this;
    }

    public AbyssPatternBuilder lastIndexAlgorithm(boolean lastIndexAlgorithm) {
        this.lastIndexAlgorithm = lastIndexAlgorithm;
        return this;
    }

    public AbyssPatternBuilder fissureToken(Token fissureToken) {
        this.fissureToken = fissureToken;
        return this;
    }

    public AbyssPatternBuilder unit(Token token) {
        return unit(token.getType(), token.getTokenValue());
    }

    public AbyssPatternBuilder unit(TokenType type, String token) {
        AbyssPatternUnit unit = new AbyssPatternUnit(type, token);
        units.add(unit);
        return this;
    }

    public AbyssPatternBuilder simpleHollow() {
        units.add(AbyssPatternTokens.SIMPLE_HOLLOW);
        return this;
    }

    public AbyssPatternBuilder hollow() {
        units.add(AbyssPatternTokens.HOLLOW);
        return this;
    }

    public AbyssPatternBuilder compile(Syntax syntax, String expression) {
        AbyssPatternCompiler compiler = new AbyssPatternCompiler(this, syntax);
        compiler.compile(expression);
        return this;
    }

    public AbyssPattern build() {
        AbyssPatternUnit[] unitsArray = new AbyssPatternUnit[units.size()];
        units.toArray(unitsArray);

        return new AbyssPattern(unitsArray, fissureToken, maxNestingLevel, keepOpposites, lastIndexAlgorithm);
    }

}
