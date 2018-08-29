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

package org.panda_lang.panda.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;

import java.util.ArrayList;
import java.util.List;

public interface TokenizedSource {

    TokenizedSource subSource(int fromIndex, int toIndex);

    TokenRepresentation[] toArray();

    default TokenizedSource selectLine(int line) {
        List<TokenRepresentation> selected = new ArrayList<>();

        for (TokenRepresentation tokenRepresentation : getTokensRepresentations()) {
            if (tokenRepresentation.getLine() < line) {
                continue;
            }

            if (tokenRepresentation.getLine() > line) {
                break;
            }

            selected.add(tokenRepresentation);
        }

        return new PandaTokenizedSource(selected);
    }

    default int size() {
        return getTokensRepresentations().size();
    }

    default String asString() {
        StringBuilder node = new StringBuilder();

        for (TokenRepresentation representation : getTokensRepresentations()) {
            Token token = representation.getToken();
            node.append(token.getTokenValue());
        }

        return node.toString();
    }

    default TokenizedSource addToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().add(tokenRepresentation);
        return this;
    }

    default @Nullable TokenRepresentation get(int id) {
        if (id >= size() || id < 0) {
            return null;
        }

        return getTokensRepresentations().get(id);
    }

    default @Nullable TokenRepresentation getLast(int i) {
        int index = size() - i - 1;
        return index > -1 ? get(index) : null;
    }

    default @Nullable String getTokenValue(int id) {
        Token token = getToken(id);

        if (token == null) {
            return null;
        }

        return token.getTokenValue();
    }

    default @Nullable Token getToken(int id) {
        TokenRepresentation tokenRepresentation = get(id);

        if (tokenRepresentation == null) {
            return null;
        }

        return tokenRepresentation.getToken();
    }

    List<TokenRepresentation> getTokensRepresentations();

    default @Nullable TokenRepresentation getFirst() {
        return size() > 0 ? get(0) : null;
    }

    default @Nullable TokenRepresentation getLast() {
        return getLast(0);
    }

}
