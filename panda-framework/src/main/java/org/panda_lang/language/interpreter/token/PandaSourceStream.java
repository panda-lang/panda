/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.token;

import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public final class PandaSourceStream implements SourceStream {

    private final Snippet original;
    private int index;

    public PandaSourceStream(Snippet source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        this.original = source;
    }

    @Override
    public TokenInfo read() {
        if (!hasUnreadSource()) {
            throw new NoSuchElementException("SourceStream is empty, cannot read next representation");
        }

        return original.get(index++);
    }

    @Override
    public Option<TokenInfo> read(Predicate<TokenInfo> condition) {
        return hasUnreadSource()
                ? Option.of(read()).filter(condition).onEmpty(() -> unread(1))
                : Option.none();
    }

    @Override
    public Snippet read(int length) {
        if (!hasUnreadSource(length)) {
            throw new IndexOutOfBoundsException("source(" + (index + length) + ") >= source.length (" + original.size() + ")");
        }

        return original.subSource(index, index += length);
    }

    @Override
    public void readSilently(int length) {
        if (!hasUnreadSource(length)) {
            throw new IndexOutOfBoundsException("source(" + (index + length) + ") >= source.length (" + original.size() + ")");
        }

        this.index += length;
    }

    @Override
    public void unread(int length) {
        this.index = Math.max(index - length, 0);
    }

    @Override
    public Snippet readLineResidue() {
        List<TokenInfo> residue = new ArrayList<>();
        int currentLine = this.getCurrentLine();

        while (this.hasUnreadSource()) {
            TokenInfo representation = this.read();

            if (representation.getLocation().getLine() != currentLine) {
                break;
            }

            residue.add(representation);
        }

        return new PandaSnippet(residue, false);
    }

    @Override
    public void dispose(int length) {
        this.index += length;
    }

    @Override
    public boolean hasUnreadSource() {
        return hasUnreadSource(1);
    }

    private boolean hasUnreadSource(int length) {
        return (index + (length - 1 )) < original.size();
    }

    @Override
    public int getUnreadLength() {
        return original.size() - index;
    }

    @Override
    public TokenInfo getNext() {
        return original.get(index + 1);
    }

    @Override
    public TokenInfo getCurrent() {
        return original.get(index);
    }

    @Override
    public Snippet getOriginalSource() {
        return original;
    }

    @Override
    public Location toLocation() {
        return getCurrent().getLocation();
    }

    @Override
    public Snippet toSnippet() {
        return original.subSource(index, original.size());
    }

    @Override
    public String toString() {
        return "PandaSourceStream['" + this.toSnippet() + "']";
    }

}
