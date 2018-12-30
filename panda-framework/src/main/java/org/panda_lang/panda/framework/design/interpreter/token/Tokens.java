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

package org.panda_lang.panda.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

import java.util.ArrayList;
import java.util.List;

public interface Tokens {

    default Tokens[] split(Token token) {
        List<Tokens> tokens = new ArrayList<>();
        int previousIndex = 0;

        for (int i = 0; i < size(); i++) {
            TokenRepresentation current = get(i);

            if (current.contentEquals(token)) {
                tokens.add(subSource(previousIndex, i - 1));
                previousIndex = i;
            }
        }

        return tokens.toArray(new Tokens[0]);
    }

    default Tokens subSource(int fromIndex, int toIndex) {
        return new PandaTokens(getTokensRepresentations().subList(fromIndex, toIndex));
    }

    default Tokens selectLine(int line) {
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

        return new PandaTokens(selected);
    }

    default Tokens addToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().add(tokenRepresentation);
        return this;
    }

    default Tokens addTokens(Tokens tokens) {
        getTokensRepresentations().addAll(tokens.getTokensRepresentations());
        return this;
    }

    default boolean startsWith(Token... tokens) {
        if (tokens.length > size()) {
            return false;
        }

        for (int i = 0; i < tokens.length; i++) {
            if (!get(i).contentEquals(tokens[i])) {
                return false;
            }
        }

        return true;
    }

    default boolean contains(Token token) {
        for (TokenRepresentation representation : getTokensRepresentations()) {
            if (representation.contentEquals(token)) {
                return true;
            }
        }

        return false;
    }

    default int size() {
        return getTokensRepresentations().size();
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean hasElement(int index) {
        return index > -1 && index < size();
    }

    default TokenRepresentation get(int index) {
        if (!hasElement(index)) {
            throw new TokensIndexOutOfBoundsException(index);
        }

        return getTokensRepresentations().get(index);
    }

    default @Nullable TokenRepresentation getLast(int lastIndex) {
        int index = size() - lastIndex - 1;
        return hasElement(index) ? get(index) : null;
    }

    default String getTokenValue(int index) {
        return getToken(index).getTokenValue();
    }

    default Token getToken(int index) {
        return get(index).getToken();
    }

    default TokenRepresentation getFirst() {
        return get(0);
    }

    default TokenRepresentation getLast() {
        return getLast(0);
    }

    default int getCurrentLine() {
        return hasElement(0) ? get(0).getLine() + 1 : -1;
    }

    default String asString() {
        StringBuilder node = new StringBuilder();

        for (TokenRepresentation representation : getTokensRepresentations()) {
            Token token = representation.getToken();
            node.append(token.getTokenValue());
        }

        return node.toString();
    }

    List<TokenRepresentation> getTokensRepresentations();

    TokenRepresentation[] toArray();

}
