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

package org.panda_lang.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.utilities.commons.collection.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Snippet is one of the most basic elements used by Panda Framework and represents a sequence of tokens.
 * It may be compared to {@link String} and {@link org.panda_lang.framework.design.interpreter.token.TokenRepresentation} to characters.
 */
public interface Snippet extends Iterable<TokenRepresentation>, Snippetable {

    /**
     * Constant for not found result
     */
    int NOT_FOUND = -1;

    /**
     * Snippet supports iterations
     *
     * @return the iterator
     */
    @Override
    default Iterator<TokenRepresentation> iterator() {
        return new SnippetIterator(this);
    }

    /**
     * Reverse tokens and get'em as a new snippet
     *
     * @return a reversed snippet
     */
    default Snippet reversed() {
        Snippet snippet = new PandaSnippet(getTokensRepresentations());
        Collections.reverse(snippet.getTokensRepresentations());
        return snippet;
    }

    /**
     * Returns a view of the portion of this snippet between the specified fromIndex, inclusive, and toIndex exclusive.
     * Returned snipped is an independent element and changes in sub snippet does not affect the original snippet.
     *
     * @param fromIndex the start index
     * @param toIndex the end index
     * @return a new snippet
     */
    default Snippet subSource(int fromIndex, int toIndex) {
        if (toIndex < 0) {
            return new PandaSnippet(Lists.subList(getTokensRepresentations(), fromIndex, size() + toIndex), false);
        }

        return new PandaSnippet(Lists.subList(getTokensRepresentations(), fromIndex, toIndex), false);
    }

    /**
     * Split snippet using the given delimiter.
     * If snippet does not contain delimiter, the method will return one item array containing copy of the current source
     *
     * @param delimiter the delimiting token
     * @return array of items
     */
    default Snippet[] split(Token delimiter) {
        List<Snippet> snippets = new ArrayList<>();
        int previousIndex = 0;

        for (int i = 0; i < size(); i++) {
            TokenRepresentation current = get(i);

            if (delimiter.equals(current.getToken())) {
                snippets.add(subSource(previousIndex, i));
                previousIndex = i + 1;
            }
        }

        if (previousIndex != 0) {
            snippets.add(subSource(previousIndex, size()));
        }
        else if (snippets.isEmpty()) {
            snippets.add(new PandaSnippet(getTokensRepresentations()));
        }

        return snippets.toArray(new Snippet[0]);
    }

    /**
     * Check if snippet starts with the given tokens
     *
     * @param tokens the tokens to compare with
     * @return true if snippet starts with the given tokens, otherwise false
     */
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

    /**
     * Check if snippet contains at least one of the given token
     *
     * @param tokens the tokens to search for
     * @return true if snippet contains the given token, otherwise false
     */
    default boolean contains(Token... tokens) {
        for (Token token : tokens) {
            if (indexOf(token) != NOT_FOUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get index of the given token in snippet
     *
     * @param token the token to search for
     * @return index of token, if snippet does not contain token, the method returns {@link #NOT_FOUND} value
     */
    default int indexOf(Token token) {
        List<? extends TokenRepresentation> tokens = getTokensRepresentations();

        for (int index = 0; index < tokens.size(); index++) {
            if (tokens.get(index).contentEquals(token)) {
                return index;
            }
        }

        return NOT_FOUND;
    }

    /**
     * Add tokens to the current snippet
     *
     * @param snippet the snippet (collection of tokens) to add
     */
    void addTokens(Snippet snippet);

    /**
     * Add token to the snippet
     *
     * @param tokenRepresentation the token to add
     */
    void addToken(TokenRepresentation tokenRepresentation);

    /**
     * Remove token from snippet
     *
     * @param tokenRepresentation the token to remove
     */
    default void removeToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().remove(tokenRepresentation);
    }

    /**
     * Remove token at the given index
     *
     * @param index the index to use
     * @return removed token
     */
    default TokenRepresentation remove(int index) {
        return getTokensRepresentations().remove(index);
    }

    /**
     * Get size of snippet
     *
     * @return amount of tokens in the snippet
     */
    default int size() {
        return getTokensRepresentations().size();
    }

    /**
     * Check if snippet is empty
     *
     * @return true if snippet does not contain any token
     */
    default boolean isEmpty() {
        return getTokensRepresentations().isEmpty();
    }

    /**
     * Check if snippet contains token at the given index
     *
     * @param index the index to check
     * @return true if there is token at the given index
     */
    default boolean hasElement(int index) {
        return index > NOT_FOUND && index < size();
    }

    /**
     * Get token at the given position
     *
     * @param index the index to use
     * @return a token at the given position
     * @throws SnippetIndexOutOfBoundsException when index is out of bounds
     */
    default TokenRepresentation get(int index) {
        if (!hasElement(index)) {
            throw new SnippetIndexOutOfBoundsException(index);
        }

        return getTokensRepresentations().get(index);
    }

    /**
     * Get the first token
     *
     * @return the first token
     */
    default TokenRepresentation getFirst() {
        return get(0);
    }

    /**
     * Get token at the given index counting backwards of snippet
     *
     * @param lastIndex the index to use
     * @return token at the given position, otherwise null
     */
    default @Nullable TokenRepresentation getLast(int lastIndex) {
        int index = size() - lastIndex - 1;
        return hasElement(index) ? get(index) : null;
    }

    /**
     * Get the last token
     *
     * @return the last token or null if the snippet is empty
     */
    default @Nullable TokenRepresentation getLast() {
        return getLast(0);
    }

    /**
     * Get tokens at the given line as a snippet
     *
     * @param line the line to search for
     * @return tokens at the requested line
     */
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

    /**
     * Get {@link org.panda_lang.framework.design.interpreter.source.SourceLocation} of first token in snippet
     *
     * @return the current location
     */
    default SourceLocation getLocation() {
        return getFirst().getLocation();
    }

    /**
     * Get snippet as source.
     * Method {@link #toString()} returns an easy to read formatted representation of snippet,
     * but the current method returns the source represented by snippet that may be still parsed.
     *
     * @return the source represented by the snippet
     */
    default String asSource() {
        StringBuilder node = new StringBuilder();

        for (TokenRepresentation representation : getTokensRepresentations()) {
            Token token = representation.getToken();

            if (token instanceof Section) {
                Section section = representation.toToken();

                node.append(section.getSeparator())
                        .append(section.getContent().asSource())
                        .append(section.getSeparator().getOpposite());

                continue;
            }

            node.append(token.getValue());
        }

        return node.toString();
    }

    /**
     * Get tokens as list
     *
     * @return the list of tokens used by snippet
     */
    List<? extends TokenRepresentation> getTokensRepresentations();

    /**
     * Convert snippet into the array of tokens
     *
     * @return a new array containing content of snippet
     */
    TokenRepresentation[] toArray();

    @Override
    default Snippet toSnippet() {
        return this;
    }

}
