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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponents;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaPipelinePath;

import java.util.Collection;

/**
 * {@link org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath} initializer
 */
public final class PipelinesInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final PipelinePath path = new PandaPipelinePath();

    PipelinesInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Add pipeline components to the path
     *
     * @param componentsClasses pipeline components classes to add
     * @return the initializer
     */
    @SafeVarargs
    public final PipelinesInitializer usePipelines(Class<? extends PipelineComponents>... componentsClasses) {
        for (Class<? extends PipelineComponents> componentClass : componentsClasses) {
            try {
                PipelineComponents pipelines = componentClass.newInstance();
                Collection<PipelineComponent<?>> components = pipelines.collectPipelineComponents();

                for (PipelineComponent<?> component : components) {
                    path.createPipeline(component);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot create instance of pipelines container: " + e.getMessage());
            }
        }

        bootstrap.logger.debug("--- Loading pipelines");
        bootstrap.logger.debug("Pipelines: (" + path.names().size() + ") " + path.names());
        bootstrap.logger.debug("");
        
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        bootstrap.resources.withPipelinePath(path);
        return bootstrap;
    }

}
