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

package org.panda_lang.panda.core.interpreter.lexer.pattern;

import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.EqualableToken;

public class TokenPatternUnit extends EqualableToken {

    private static final TokenType HOLLOW_TYPE = new TokenType("HOLLOW");

    /**
     * Multiline gap
     */
    public static final TokenPatternUnit HOLLOW = new TokenPatternUnit(HOLLOW_TYPE, "*");

    /**
     * Inline gap
     */
    public static final TokenPatternUnit SIMPLE_HOLLOW = new TokenPatternUnit(HOLLOW_TYPE, "**");

    private final TokenType tokenType;
    private final String token;

    public TokenPatternUnit(TokenType tokenType, String token) {
        this.tokenType = tokenType;
        this.token = token;
    }

    /**
     * @return true if unit is a gap of any type
     */
    public boolean isGap() {
        return HOLLOW.equals(this) || SIMPLE_HOLLOW.equals(this);
    }

    /**
     * @return true if unit is a gap in inline scope
     */
    public boolean isFissure() {
        return SIMPLE_HOLLOW.equals(this);
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
