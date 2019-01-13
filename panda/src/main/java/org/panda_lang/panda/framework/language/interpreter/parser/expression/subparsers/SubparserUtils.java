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

package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.utilities.commons.CharacterUtils;

import java.util.function.Predicate;

class SubparserUtils {

    public static Predicate<Token> NAMES_FILTER = token -> token.getType() == TokenType.UNKNOWN && !isAllowedName(token);

    private static final char[] ALLOWED_START = CharacterUtils.mergeArrays(
            CharacterUtils.LETTERS,
            CharacterUtils.arrayOf('$', '_')
    );

    private static final char[] ALLOWED = CharacterUtils.mergeArrays(
            ALLOWED_START,
            CharacterUtils.DIGITS
    );

    static @Nullable Tokens readFirstOfType(Tokens source, TokenType type) {
        TokenRepresentation token = source.get(0);

        if (token.getToken().getType() != type) {
            return null;
        }

        return new PandaTokens(token);
    }

    static @Nullable Tokens readBetweenSeparators(Tokens source, Separator first) {
        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));

        if (!matchable.nextVerified().contentEquals(first)) {
            return null;
        }

        matchable.nextVerified();

        while (matchable.hasNext() && !matchable.isMatchable()) {
            matchable.nextVerified();
        }

        if (!matchable.isMatchable()) {
            return null;
        }

        return source.subSource(0, matchable.getIndex());
    }


    static boolean isAllowedName(Token token) {
        if (token.getType() != TokenType.UNKNOWN) {
            return false;
        }

        if (!CharacterUtils.belongsTo(token.getTokenValue().charAt(0), ALLOWED_START)) {
            return false;
        }

        char[] chars = token.getTokenValue().toCharArray();

        for (int i = 1; i < token.getTokenValue().toCharArray().length; i++) {
            char c = chars[i];

            if (!CharacterUtils.belongsTo(c, ALLOWED)) {
                return false;
            }
        }

        return true;
    }


}
