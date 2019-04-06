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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.defaults;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

class TypeReader implements WildcardReader<Snippet> {

    @Override
    public boolean match(String data) {
        return data.startsWith("type");
    }

    @Override
    public @Nullable Snippet read(ParserData data, String content, TokenDistributor distributor) {
        TokenRepresentation type = distributor.next();

        if (type.getToken().getType() != TokenType.UNKNOWN) {
            return null;
        }

        PandaSnippet tokens = new PandaSnippet(type);

        while (mayNext(distributor)) {
            Snippet next = read(distributor);

            if (next == null) {
                break;
            }

            tokens.addTokens(next);
        }

        return tokens;
    }

    private boolean mayNext(TokenDistributor distributor) {
        if (distributor.size() < 2) {
            return false;
        }

        TokenRepresentation next = distributor.getNext();

        if (next == null) {
            return false;
        }

        if (next.contentEquals(Separators.SQUARE_BRACKET_LEFT)) {
            return true;
        }

        return next.contentEquals(Operators.LESS_THAN);
    }

    private @Nullable Snippet read(TokenDistributor distributor) {
        TokenRepresentation next = distributor.next();

        if (next.contentEquals(Separators.SQUARE_BRACKET_LEFT)) {
            return distributor.getNext().contentEquals(Separators.SQUARE_BRACKET_RIGHT) ? new PandaSnippet(next, distributor.next()) : null;
        }

        if (next.contentEquals(Operators.GREATER_THAN)) {
            throw new PandaFrameworkException("Angle brackets not implemented yet");
        }

        return null;
    }

}
