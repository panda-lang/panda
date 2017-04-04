/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.core.interpreter.parser.pipeline.registry;

import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.core.interpreter.parser.pipeline.PandaParserPipeline;

import java.util.HashMap;
import java.util.Map;

public class PandaPipelineRegistry implements PipelineRegistry {

    private final Map<String, ParserPipeline> pipelines;

    public PandaPipelineRegistry() {
        this.pipelines = new HashMap<>();
    }

    protected ParserPipeline getOrCreate(String pipelineName) {
        ParserPipeline pipeline = getPipeline(pipelineName);

        if (pipeline == null) {
            pipeline = new PandaParserPipeline();
            pipelines.put(pipelineName, pipeline);
        }

        return pipeline;
    }

    @Override
    public ParserPipeline getPipeline(String pipelineName) {
        return pipelines.get(pipelineName);
    }

}
