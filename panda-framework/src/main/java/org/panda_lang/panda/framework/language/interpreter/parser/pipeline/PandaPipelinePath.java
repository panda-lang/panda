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

package org.panda_lang.panda.framework.language.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.AbstractComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaPipelinePath implements PipelinePath {

    private final Map<PipelineComponent<?>, ParserPipeline<?>> pipelines = new HashMap<>(3);

    public PandaPipelinePath() {
        pipelines.put(UniversalPipelines.ALL, new PandaParserPipeline<>(UniversalPipelines.ALL.getName()));
    }

    @Override
    public <P extends Parser> ParserPipeline<P> createPipeline(PipelineComponent<P> component) {
        ParserPipeline<P> pipeline = getPipeline(component);

        if (pipeline == null) {
            pipelines.put(component, new PandaParserPipeline<>(pipelines.get(UniversalPipelines.ALL), component.getName()));
            pipeline = getPipeline(component);
        }

        return pipeline;
    }

    @Override
    public boolean hasPipeline(PipelineComponent<?> component) {
        return getPipeline(component) != null;
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
