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

import org.panda_lang.framework.interpreter.source.Localizable;
import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.utilities.commons.function.Option;

import java.util.function.Predicate;

/**
 * Represents stream of tokens
 */
public interface SourceStream extends Snippetable, Localizable {

    /**
     * Read the next token
     *
     * @return the next token
     */
    TokenInfo read();

    /**
     * Conditional read of next token
     *
     * @param condition the method will read the token only if this condition returns true
     * @return the optional result
     */
    Option<TokenInfo> read(Predicate<TokenInfo> condition);

    /**
     * Read the specified amount of tokens
     *
     * @param length amount of tokens to read
     * @return the amount of tokens to read
     */
    Snippet read(int length);

    /**
     * Unread the given amount of tokens
     *
     * @param length the amount of tokens to unread
     */
    void unread(int length);

    /**
     * Read the specified amount of tokens without creating snippet of read content
     *
     * @param length the amount of tokens to read
     */
    void readSilently(int length);

    /**
     * Read the specified amount of tokens and dispose result (it won't create snippet)
     */
    void dispose(int length);

    default void dispose(SourceStream stream) {
        dispose(stream.getReadLength());
    }

    /**
     * Read the rest of the current line
     *
     * @return the rest of the source at the current line
     */
    Snippet readLineResidue();

    /**
     * Check if the source has available content
     *
     * @return true if source contains available for read content
     */
    boolean hasUnreadSource();

    /**
     * Get next token without moving index
     *
     * @return the next token
     */
    TokenInfo getNext();

    /**
     * Get current token
     *
     * @return current token
     */
    TokenInfo getCurrent();

    /**
     * Get original source used to create the stream
     *
     * @return the original source
     */
    Snippet getOriginalSource();

    /**
     * Get current line
     *
     * @return if there is no available source, the method returns -2, otherwise returns the number of current line
     */
    default int getCurrentLine() {
        return hasUnreadSource() ? toSnippet().getFirst().getLocation().getLine() : Location.UNKNOWN_LOCATION;
    }

    /**
     * Get the amount of read tokens
     *
     * @return the amount of read tokens
     */
    default int getReadLength() {
        return getOriginalSource().size() - getUnreadLength();
    }

    /**
     * Get the amount of unread tokens
     *
     * @return the amount of unread tokens
     */
    int getUnreadLength();

    /**
     * Get current source as snippet
     *
     * @return the current content wrapped in snippet
     */
    @Override
    Snippet toSnippet();

    @Override
    default SourceStream toStream() {
        return this;
    }

}
