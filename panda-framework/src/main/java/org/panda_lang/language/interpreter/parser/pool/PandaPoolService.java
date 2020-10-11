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

package org.panda_lang.language.interpreter.parser.pool;

import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.StreamUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PandaPoolService implements PoolService {

    private final Map<Component<?>, ParserPool<?>> pipelines = new HashMap<>(3);

    public PandaPoolService() {
        pipelines.put(Targets.ALL, new PandaParserPool<>(Targets.ALL.getName()));
    }

    @Override
    public <T> ParserPool<T> computeIfAbsent(Component<T> component) {
        ParserPool<T> parserPool = getPool(component);

        if (parserPool == null) {
            pipelines.put(component, new PandaParserPool<>(pipelines.get(Targets.ALL), component.getName()));
            parserPool = getPool(component);
        }

        return parserPool;
    }

    @Override
    public boolean hasPool(Component<?> component) {
        return getPool(component) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> ParserPool<P> getPool(Component<P> component) {
        return (ParserPool<P>) pipelines.get(component);
    }

    @Override
    public Collection<String> names() {
        return StreamUtils.map(pipelines.keySet(), Component::getName);
    }

}
