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

package org.panda_lang.panda.framework.language.interpreter.token.stream;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PandaSourceStream implements SourceStream {

    private final Snippet original;
    private int index;

    public PandaSourceStream(Snippet source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        this.original = source;
    }

    @Override
    public TokenRepresentation read() {
        if (!hasUnreadSource()) {
            throw new NoSuchElementException("SourceStream is empty, cannot read next representation");
        }

        return original.get(index++);
    }

    @Override
    public Snippet read(int length) {
        if (!hasUnreadSource(length)) {
            throw new IndexOutOfBoundsException("source(" + (index + length) + ") >= source.length (" + original.size() + ")");
        }

        return original.subSource(index, index += length);
    }

    @Override
    public Snippet readLineResidue() {
        List<TokenRepresentation> residue = new ArrayList<>();
        int currentLine = this.getCurrentLine();

        while (this.hasUnreadSource()) {
            TokenRepresentation representation = this.read();

            if (representation.getLocation().getLine() != currentLine) {
                break;
            }

            residue.add(representation);
        }

        return new PandaSnippet(residue, false);
    }

    @Override
    public void dispose(int length) {
        index += length;
    }

    @Override
    public Snippet getOriginalSource() {
        return original;
    }

    @Override
    public Snippet toSnippet() {
        return original.subSource(index, original.size());
    }

    @Override
    public boolean hasUnreadSource() {
        return hasUnreadSource(1);
    }

    private boolean hasUnreadSource(int length) {
        return (index + (length - 1)) < original.size();
    }

    @Override
    public String toString() {
        return "PandaSourceStream['" + this.toSnippet() + "']";
    }

}
