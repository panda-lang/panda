/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.interpreter.source.Localizable;
import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.auxiliary.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Snippet (AST - Abstract Syntax Tree) is one of the most basic structures used by the Panda Framework.
 * It is a tree representation of the abstract syntactic structure of source code.
 */
public interface Snippet extends Iterable<TokenInfo>, Localizable, Snippetable {

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
    default Iterator<TokenInfo> iterator() {
        return new SnippetIterator(this);
    }

    /**
     * Reverse tokens and get'em as a new snippet
     *
     * @return a reversed snippet
     */
    default Snippet reversed() {
        Snippet snippet = PandaSnippet.ofImmutable(new ArrayList<>(getTokensRepresentations()));
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
            return new PandaSnippet(getTokensRepresentations().subList(fromIndex, size() + toIndex), isMutable());
        }

        return new PandaSnippet(getTokensRepresentations().subList(fromIndex, toIndex), isMutable());
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
            TokenInfo current = get(i);

            if (delimiter.equals(current.getToken())) {
                snippets.add(subSource(previousIndex, i));
                previousIndex = i + 1;
            }
        }

        if (previousIndex != 0) {
            snippets.add(subSource(previousIndex, size()));
        }
        else if (snippets.isEmpty()) {
            snippets.add(new PandaSnippet(getTokensRepresentations(), isMutable()));
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

    default boolean contains(Token token) {
        return indexOf(token) != NOT_FOUND;
    }

    /**
     * Check if snippet contains at least one of the given token
     *
     * @param tokens the tokens to search for
     * @return true if snippet contains the given token, otherwise false
     */
    default boolean contains(Token... tokens) {
        for (Token token : tokens) {
            if (contains(token)) {
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
        List<? extends TokenInfo> tokens = getTokensRepresentations();

        for (int index = 0; index < tokens.size(); index++) {
            if (tokens.get(index).contentEquals(token)) {
                return index;
            }
        }

        return NOT_FOUND;
    }

    /**
     * Add token to the snippet
     *
     * @param tokenInfo the token to add
     */
    void append(TokenInfo tokenInfo);

    /**
     * Remove token at the given index
     *
     * @param index the index to use
     * @return removed token
     */
    TokenInfo remove(int index);

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
    default TokenInfo get(int index) {
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
    default TokenInfo getFirst() {
        return get(0);
    }

    /**
     * Get token at the given index counting backwards of snippet
     *
     * @param lastIndex the index to use
     * @return token at the given position, otherwise null
     */
    default @Nullable TokenInfo getLast(int lastIndex) {
        int index = size() - lastIndex - 1;
        return hasElement(index) ? get(index) : null;
    }

    /**
     * Get the last token
     *
     * @return the last token or null if the snippet is empty
     */
    default TokenInfo getLast() {
        return getLast(0);
    }

    default Snippet getFirstLine() {
        if (isEmpty()) {
            return this;
        }

        return getLine(getFirst().getLocation().getLine());
    }

    default Snippet getLastLine() {
        if (isEmpty()) {
            return this;
        }

        return getLine(getLast().getLocation().getLine());
    }

    /**
     * Get tokens at the given line as a snippet
     *
     * @param line the line to search for
     * @return tokens at the requested line
     */
    default Snippet getLine(int line) {
        List<TokenInfo> selected = new ArrayList<>();

        for (TokenInfo tokenInfo : getTokensRepresentations()) {
            if (tokenInfo.getType() == TokenTypes.SECTION) {
                Snippet content = tokenInfo.toToken(Section.class).getContent();
                Snippet selectedContent = content.getLine(line);

                if (!selectedContent.isEmpty()) {
                    if (!content.equals(selectedContent)) {
                        return selectedContent;
                    }

                    selected.add(tokenInfo);
                    continue;
                }
            }

            if (tokenInfo.getLocation().getLine() < line) {
                continue;
            }

            if (tokenInfo.getLocation().getLine() > line) {
                break;
            }

            selected.add(tokenInfo);
        }

        return PandaSnippet.ofImmutable(selected);
    }

    /**
     * Get {@link org.panda_lang.framework.interpreter.source.Location} of first token in snippet
     *
     * @return the current location
     */
    default Location getLocation() {
        return getFirst().getLocation();
    }

    /**
     * Get snippet as source.
     * Method toString() returns an easy to read formatted representation of snippet,
     * but the current method returns the source represented by snippet that may be still parsed.
     *
     * @return the source represented by the snippet
     */
    default String asSource() {
        StringBuilder node = new StringBuilder();

        for (TokenInfo representation : getTokensRepresentations()) {
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
    List<? extends TokenInfo> getTokensRepresentations();

    /**
     * Convert snippet into the array of tokens
     *
     * @return a new array containing content of snippet
     */
    TokenInfo[] toArray();

    /**
     * Check if snippet is marked as immutable
     *
     * @return true if immutable
     */
    default boolean isImmutable() {
        return !isMutable();
    }

    /**
     * Check if snippet is marked as mutable
     *
     * @return true if mutable
     */
    boolean isMutable();

    @Override
    default Location toLocation() {
        return getLocation();
    }

    @Override
    default Snippet toSnippet() {
        return this;
    }

}
