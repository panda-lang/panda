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

package org.panda_lang.language.architecture.module;

import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.interpreter.source.SourceService;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PandaModulePath extends PandaModuleContainer implements ModulePath {

    private final SourceService sources;
    private final Map<String, Pair<Completable<Option<Script>>, Function<SourceService, Completable<Script>>>> modulesInitializers = new HashMap<>();

    public PandaModulePath(SourceService sources) {
        this.sources = sources;
    }

    @Override
    public Completable<Option<Script>> include(String name, Function<SourceService, Completable<Script>> initializer) {
        Pair<Completable<Option<Script>>, Function<SourceService, Completable<Script>>> cachedInitialize = modulesInitializers.get(name);

        if (cachedInitialize != null) {
            Function<SourceService, Completable<Script>> nextInitialize = initializer;

            initializer = src -> {
                cachedInitialize.getValue().apply(sources);
                return nextInitialize.apply(sources);
            };
        }

        Completable<Option<Script>> script = new Completable<>();
        modulesInitializers.put(name, new Pair<>(script, initializer));

        return script;
    }

    @Override
    public Completable<Option<Module>> forModule(String moduleQualifier) {
        Pair<Completable<Option<Script>>, Function<SourceService, Completable<Script>>> initialize = modulesInitializers.get(moduleQualifier);

        if (initialize != null) {
            modulesInitializers.remove(moduleQualifier);

            return initialize.getValue().apply(sources)
                    .then(script -> initialize.getKey().complete(Option.of(script)))
                    .thenCompose(script -> forModule(moduleQualifier));
        }

        return super.forModule(moduleQualifier);
    }

}
