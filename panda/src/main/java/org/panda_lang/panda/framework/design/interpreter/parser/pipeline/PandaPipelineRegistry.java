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

package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.AbstractComponent;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaPipelineRegistry implements PipelineRegistry {

    private final Map<PipelineComponent<?>, ParserPipeline<?>> pipelines = new HashMap<>(3);

    public PandaPipelineRegistry() {
        pipelines.put(UniversalPipelines.ALL, new PandaParserPipeline<>());
    }

    protected <P extends Parser> ParserPipeline<P> getOrCreate(PipelineComponent<P> component) {
        ParserPipeline<P> pipeline = getPipeline(component);

        if (pipeline == null) {
            pipelines.put(component, new PandaParserPipeline<>(pipelines.get(UniversalPipelines.ALL)));
            pipeline = getPipeline(component);
        }

        return pipeline;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends Parser> ParserPipeline<P> getPipeline(PipelineComponent<P> component) {
        return (ParserPipeline<P>) pipelines.get(component);
    }

    @Override
    public long getTotalHandleTime() {
        return StreamUtils.sumLongs(pipelines.values(), ParserPipeline::getHandleTime);
    }

    @Override
    public Collection<String> names() {
        return StreamUtils.map(pipelines.keySet(), AbstractComponent::getName);
    }

}
