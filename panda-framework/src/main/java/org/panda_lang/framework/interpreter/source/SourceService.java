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

package org.panda_lang.framework.interpreter.source;

import org.panda_lang.framework.architecture.packages.Script;
import org.panda_lang.framework.interpreter.Interpreter;
import org.panda_lang.utilities.commons.collection.Pair;
import panda.std.Completable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * Set of {@link Source} used by {@link Interpreter}
 */
public class SourceService {

    private final Map<String, Script> loaded = new LinkedHashMap<>();
    private final Stack<Pair<? extends Source, Completable<Script>>> sources = new Stack<>();

    /**
     * Add a new source to set
     *
     * @param sources sources to load
     */
    public Collection<Pair<? extends Source, Completable<Script>>> pushSources(Collection<? extends Source> sources) {
        return sources.stream()
                .map(source -> new Pair<>(source, new Completable<Script>()))
                .peek(this.sources::add)
                .collect(Collectors.toList());
    }

    public Pair<? extends Source, Completable<Script>> retrieve() {
        Pair<? extends Source, Completable<Script>> currentSource = sources.pop();
        currentSource.getValue().then(script -> loaded.put(script.getName(), script));
        return currentSource;
    }

    public boolean hasUnloadedSources() {
        return !sources.isEmpty();
    }

}
