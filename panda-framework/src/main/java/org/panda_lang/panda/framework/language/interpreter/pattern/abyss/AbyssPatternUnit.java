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

import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.EqualableToken;

public class AbyssPatternUnit extends EqualableToken {

    private final TokenType tokenType;
    private final String token;
    private final boolean optional;

    public AbyssPatternUnit(TokenType tokenType, String token, boolean optional) {
        this.tokenType = tokenType;
        this.token = token;
        this.optional = optional;
    }

    public AbyssPatternUnit(TokenType tokenType, String token) {
        this(tokenType, token, false);
    }

    /**
     * @return true if unit is a gap of any type
     */
    public boolean isAbyss() {
        return AbyssPatternTokens.ABYSS.equals(this) || AbyssPatternTokens.SIMPLE_ABYSS.equals(this);
    }

    /**
     * @return true if unit is a gap in inline scope
     */
    public boolean isSimpleAbyss() {
        return AbyssPatternTokens.SIMPLE_ABYSS.equals(this);
    }

    /**
     * @return true if unit is optional
     */
    public boolean isOptional() {
        return optional;
    }

    @Override
    public String getTokenValue() {
        return token;
    }

    @Override
    public TokenType getType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return tokenType.getTypeName() + ": " + token;
    }

}
