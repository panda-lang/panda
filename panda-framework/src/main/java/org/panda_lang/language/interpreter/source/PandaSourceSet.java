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

package org.panda_lang.language.interpreter.source;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class PandaSourceSet implements SourceSet {

    private final List<Source> sources;

    public PandaSourceSet() {
        this.sources = new LinkedList<>();
    }

    @Override
    public Iterator<Source> iterator() {
        return new Iterator<Source>() {

            private int index;

            @Override
            public boolean hasNext() {
                return index < sources.size();
            }

            @Override
            public Source next() {
                return sources.get(index++);
            }

        };
    }

    @Override
    public void addSource(Source source) {
        this.sources.add(source);
    }

    @Override
    public boolean isEmpty() {
        return sources.isEmpty();
    }

}
