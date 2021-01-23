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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.language.interpreter.parser.pool.PoolService;
import org.panda_lang.language.interpreter.parser.pool.PandaPoolService;
import org.panda_lang.language.interpreter.parser.Component;

import java.util.Collection;

/**
 * {@link org.panda_lang.language.interpreter.parser.pool.PoolService} initializer
 */
public final class PipelinesInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final PoolService poolService = new PandaPoolService();

    PipelinesInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Add pipeline components to the path
     *
     * @param componentsCollections pipeline components to add
     * @return the initializer
     */
    @SafeVarargs
    public final PipelinesInitializer usePipelines(Collection<Component<?>>... componentsCollections) {
        for (Collection<Component<?>> components : componentsCollections) {
            for (Component<?> component : components) {
                poolService.computeIfAbsent(component);
            }
        }

        bootstrap.logger.debug("--- Loading pipelines");
        bootstrap.logger.debug("Pipelines: (" + poolService.names().size() + ") " + poolService.names());
        bootstrap.logger.debug("");

        return this;
    }

    @Override
    public PandaBootstrap collect() {
        bootstrap.resources.withPipelinePath(poolService);
        return bootstrap;
    }

}
