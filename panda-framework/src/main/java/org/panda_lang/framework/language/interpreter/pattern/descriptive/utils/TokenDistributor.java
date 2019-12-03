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

package org.panda_lang.framework.language.interpreter.pattern.descriptive.utils;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class TokenDistributor implements Iterable<TokenRepresentation> {

    private final Snippet source;
    private int index;

    public TokenDistributor(Snippet source) {
        this.source = source;
    }

    @Override
    public Iterator<TokenRepresentation> iterator() {
        return new Iterator<TokenRepresentation>() {
            private int i = index;

            @Override
            public boolean hasNext() {
                return i < source.size();
            }

            @Override
            public TokenRepresentation next() {
                return source.get(i++);
            }
        };
    }

    public TokenRepresentation next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return source.get(index++);
    }

    public List<TokenRepresentation> next(int size) {
        List<TokenRepresentation> representations = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            representations.add(next());
        }

        return representations;
    }

    public Snippet currentSubSource() {
        return subSource(getIndex(), size());
    }

    public Snippet subSource(int startIndex, int endIndex) {
        return source.subSource(startIndex, endIndex);
    }

    public int size() {
        return source.size();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean hasNext() {
        return index < size();
    }

    public @Nullable TokenRepresentation current() {
        return source.hasElement(index - 1) ? source.get(index - 1) : null;
    }

    public @Nullable TokenRepresentation get(int index) {
        return source.get(index);
    }

    public @Nullable TokenRepresentation getNext() {
        return source.hasElement(index) ? source.get(index) : null;
    }

    public int getIndex() {
        return index;
    }

    public Snippet getSource() {
        return source;
    }

}
