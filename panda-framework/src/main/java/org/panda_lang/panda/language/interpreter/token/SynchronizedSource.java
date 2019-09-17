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

package org.panda_lang.panda.language.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;

import java.util.Iterator;

/**
 * SynchronizedSource works like a shared iterator with its own index and source based on {@link org.panda_lang.panda.framework.design.interpreter.token.Snippet}
 */
public class SynchronizedSource implements Iterable<TokenRepresentation>, Iterator<TokenRepresentation> {

    private final Snippet source;
    private int index;
    private int cachedIndex;

    public SynchronizedSource(Snippet source) {
        this.source = source;
    }

    /**
     * Backup the current index
     */
    public void cacheIndex() {
        this.cachedIndex = index;
    }

    @Override
    public Iterator<TokenRepresentation> iterator() {
        return this;
    }

    /**
     * Read the next token and move index
     *
     * @return the next token
     *
     * @see #hasNext()
     */
    @Override
    public TokenRepresentation next() {
        return source.get(index++);
    }

    /**
     * Check if source has available next token
     *
     * @return true if there is token to read
     */
    @Override
    public boolean hasNext() {
        return source.hasElement(index);
    }

    /**
     * Set current index of source
     *
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
        this.cacheIndex();
    }

    /**
     * Get previus token
     *
     * @return the previous token or null if index is 0
     *
     * @see #getPrevious(int)
     */
    public @Nullable TokenRepresentation getPrevious() {
        return getPrevious(0);
    }

    /**
     * Get previous token
     *
     * @param previous amount of previous tokens to skip (by default 0 - returns previous token)
     * @return requested token or if it's out of index - null
     */
    public @Nullable TokenRepresentation getPrevious(int previous) {
        int previousIndex = index - 2 - previous;
        return previousIndex < 0 ? null : source.get(previousIndex);
    }

    /**
     * Get current token
     *
     * @return the current token
     */
    public TokenRepresentation getCurrent() {
        return source.get(index - 1);
    }

    /**
     * Get next token without updating index
     *
     * @return the next token
     */
    public TokenRepresentation getNext() {
        return source.get(index);
    }

    /**
     * Get source that has been read since the last backup
     *
     * @return the last read source
     */
    public Snippet getLastReadSource() {
        return source.subSource(cachedIndex, index);
    }

    /**
     * Get available source to read as snippet
     *
     * @return the snippet with available to read source
     */
    public Snippet getAvailableSource() {
        return source.subSource(index, source.size());
    }

    /**
     * Get amount of source that is available to read
     *
     * @return the amount of source
     */
    public int getAmountOfAvailableSource() {
        return source.size() - index;
    }

    /**
     * Get cached index
     *
     * @return the cached index
     */
    public int getCachedIndex() {
        return cachedIndex;
    }

    /**
     * Get current index
     *
     * @return the current index
     */
    public int getIndex() {
        return index;
    }

    /**
     * The original source used to create {@link SynchronizedSource}
     *
     * @return the original source
     */
    public Snippet getSource() {
        return source;
    }

}
