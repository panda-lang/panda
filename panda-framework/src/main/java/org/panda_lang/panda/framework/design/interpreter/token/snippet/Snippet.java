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

package org.panda_lang.panda.framework.design.interpreter.token.snippet;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface Snippet extends Snippetable, Iterable<TokenRepresentation>{

    @Override
    default Iterator<TokenRepresentation> iterator() {
        return new SnippetIterator(this);
    }

    default Snippet reversed() {
        Snippet snippet = new PandaSnippet(getTokensRepresentations());
        Collections.reverse(snippet.getTokensRepresentations());
        return snippet;
    }

    default Snippet[] split(Token token) {
        List<Snippet> tokens = new ArrayList<>();
        int previousIndex = 0;

        for (int i = 0; i < size(); i++) {
            TokenRepresentation current = get(i);

            if (current.contentEquals(token)) {
                tokens.add(subSource(previousIndex, i - 1));
                previousIndex = i;
            }
        }

        return tokens.toArray(new Snippet[0]);
    }

    default Snippet subSource(int fromIndex, int toIndex) {
        if (toIndex < 0) {
            return new PandaSnippet(getTokensRepresentations().subList(fromIndex, size() + toIndex), false);
        }

        return new PandaSnippet(getTokensRepresentations().subList(fromIndex, toIndex), false);
    }

    default Snippet getLine(int line) {
        List<TokenRepresentation> selected = new ArrayList<>();

        for (TokenRepresentation tokenRepresentation : getTokensRepresentations()) {
            if (tokenRepresentation.getLocation().getLine() < line) {
                continue;
            }

            if (tokenRepresentation.getLocation().getLine() > line) {
                break;
            }

            selected.add(tokenRepresentation);
        }

        return new PandaSnippet(selected, false);
    }

    default int indexOf(Token token) {
        List<TokenRepresentation> tokens = getTokensRepresentations();

        for (int i = 0; i < getTokensRepresentations().size(); i++) {
            if (tokens.get(i).contentEquals(token)) {
                return i;
            }
        }

        return -1;
    }

    default Snippet addToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().add(tokenRepresentation);
        return this;
    }

    default Snippet addTokens(Snippet snippet) {
        getTokensRepresentations().addAll(snippet.getTokensRepresentations());
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
        return getTokensRepresentations().isEmpty();
    }

    default boolean hasElement(int index) {
        return index > -1 && index < size();
    }

    default TokenRepresentation get(int index) {
        if (!hasElement(index)) {
            throw new SnippetIndexOutOfBoundsException(index);
        }

        return getTokensRepresentations().get(index);
    }

    default @Nullable TokenRepresentation getLast(int lastIndex) {
        int index = size() - lastIndex - 1;
        return hasElement(index) ? get(index) : null;
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

    default SourceLocation getCurrentLocation() {
        return hasElement(0) ? get(0).getLocation() : null;
    }

    default String asString() {
        StringBuilder node = new StringBuilder();

        for (TokenRepresentation representation : getTokensRepresentations()) {
            Token token = representation.getToken();

            if (token instanceof Section) {
                Section section = representation.toToken();

                node.append(section.getSeparator())
                        .append(section.getContent().asString())
                        .append(section.getSeparator().getOpposite());

                continue;
            }

            node.append(token.getValue());
        }

        return node.toString();
    }

    List<TokenRepresentation> getTokensRepresentations();

    TokenRepresentation[] toArray();

    @Override
    default Snippet toSnippet() {
        return this;
    }

}
