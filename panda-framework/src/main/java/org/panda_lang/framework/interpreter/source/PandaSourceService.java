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

import org.panda_lang.framework.architecture.Script;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Completable;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public final class PandaSourceService implements SourceService {

    private final Map<String, Pair<Source, Completable<Script>>> loaded = new LinkedHashMap<>();

    private final Queue<Pair<Source, Completable<Script>>> required = new ArrayDeque<>();
    private final Queue<Pair<Source, Completable<Script>>> standard = new ArrayDeque<>();

    @Override
    public Completable<Script> addSource(Priority priority, Source source) {
        Completable<Script> futureScript = new Completable<>();
        Pair<Source, Completable<Script>> entry = new Pair<>(source, futureScript);

        switch (priority) {
            case REQUIRED:
                this.required.offer(entry);
                break;
            case STANDARD:
                this.standard.offer(entry);
                break;
        }

        return futureScript;
    }

    private Pair<Source, Completable<Script>> retrieve(Pair<Source, Completable<Script>> source) {
        loaded.put(source.getKey().getId(), source);
        return source;
    }

    @Override
    public boolean hasRequired() {
        return !required.isEmpty();
    }

    @Override
    public Pair<Source, Completable<Script>> retrieveRequired() {
        return Objects.requireNonNull(retrieve(required.poll()));
    }

    public boolean hasStandard() {
        return !standard.isEmpty();
    }

    @Override
    public Pair<Source, Completable<Script>> retrieveStandard() {
        return Objects.requireNonNull(retrieve(standard.poll()));
    }

    public Map<String, Pair<Source, Completable<Script>>> getLoaded() {
        return loaded;
    }

}
