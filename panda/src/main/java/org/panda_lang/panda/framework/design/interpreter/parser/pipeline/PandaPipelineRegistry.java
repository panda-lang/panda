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

import java.util.HashMap;
import java.util.Map;

public class PandaPipelineRegistry implements PipelineRegistry {

    private final Map<PipelineComponent<?>, ParserPipeline<?>> pipelines = new HashMap<>(3);
    private final PandaParserPipeline<Parser> all = new PandaParserPipeline<>();

    public PandaPipelineRegistry() {
        pipelines.put(UniversalPipelines.ALL, all);
    }

    protected <P extends Parser> ParserPipeline<P> getOrCreate(PipelineComponent<P> component) {
        ParserPipeline<P> pipeline = getPipeline(component);

        if (pipeline == null) {
            pipelines.put(component, new PandaParserPipeline<>(all));
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
        long totalHandleTime = 0;

        for (ParserPipeline pipeline : pipelines.values()) {
            totalHandleTime += pipeline.getHandleTime();
        }

        return totalHandleTime;
    }

}
