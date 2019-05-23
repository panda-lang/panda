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

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaPipelinePath;

import java.util.Collection;

public class PipelinePandaBootstrap implements PandaBootstrapElement {

    private final PandaBootstrap bootstrap;
    private final PipelinePath path = new PandaPipelinePath();

    public PipelinePandaBootstrap(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @SafeVarargs
    public final PipelinePandaBootstrap usePipelines(Class<? extends Pipelines>... componentsClasses) {
        for (Class<? extends Pipelines> componentClass : componentsClasses) {
            try {
                Pipelines pipelines = componentClass.newInstance();
                Collection<PipelineComponent<?>> components = pipelines.collectPipelineComponents();

                for (PipelineComponent<?> component : components) {
                    path.createPipeline(component);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot create instance of pipelines container: " + e.getMessage());
            }
        }

        PandaFramework.getLogger().debug("--- Loading pipelines");
        PandaFramework.getLogger().debug("Pipelines: (" + path.names().size() + ") " + path.names());

        return this;
    }

    @Override
    public PandaBootstrap collect() {
        return bootstrap.withPipelinePath(path);
    }

}
