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

import org.panda_lang.framework.design.interpreter.source.SourceLocation;

/**
 * Represents stream of tokens
 */
public interface SourceStream extends Snippetable {

    /**
     * Read the next token
     *
     * @return the next token
     */
    TokenRepresentation read();

    /**
     * Read the specified amount of tokens
     *
     * @param length amount of tokens to read
     * @return the read tokens
     */
    Snippet read(int length);

    /**
     * Read the specified amount of tokens and dispose result (it won't create snippet)
     */
    void dispose(int length);

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
     * Get current token
     *
     * @return current token
     */
    TokenRepresentation getCurrent();

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
        return hasUnreadSource() ? toSnippet().getFirst().getLocation().getLine() : SourceLocation.UNKNOWN_LOCATION;
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
    default int getUnreadLength() {
        return toSnippet().size();
    }

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
