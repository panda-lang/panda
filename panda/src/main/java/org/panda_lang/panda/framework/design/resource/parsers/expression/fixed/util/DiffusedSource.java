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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.Iterator;

public class DiffusedSource implements Iterable<TokenRepresentation>, Iterator<TokenRepresentation> {

    private final Snippet source;
    private int index;
    private int backup;

    public DiffusedSource(Snippet source) {
        this.source = source;
    }

    @Override
    public Iterator<TokenRepresentation> iterator() {
        return this;
    }

    @Override
    public TokenRepresentation next() {
        return source.get(index++);
    }

    public void backup() {
        this.backup = index;
    }

    public void restore() {
        this.index = backup;
    }

    @Override
    public boolean hasNext() {
        return source.hasElement(index);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Snippet getAvailableSource() {
        return source.subSource(index, source.size());
    }

    public TokenRepresentation getCurrent() {
        return source.get(index - 1);
    }

    public TokenRepresentation getNext() {
        return source.get(index);
    }

    public int getIndex() {
        return index;
    }

}
