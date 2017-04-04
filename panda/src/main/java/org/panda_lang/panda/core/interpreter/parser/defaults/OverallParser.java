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

package org.panda_lang.panda.core.interpreter.parser.defaults;

import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.implementation.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;

public class OverallParser {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final SourceStream sourceStream;
    private final ParserGeneration generation;

    public OverallParser(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;

        PipelineRegistry pipelineRegistry = parserInfo.getComponent(Components.PIPELINE_REGISTRY);
        this.pipeline = pipelineRegistry.getPipeline(DefaultPipelines.OVERALL);

        this.sourceStream = parserInfo.getComponent(Components.SOURCE_STREAM);
        this.generation = parserInfo.getComponent(Components.GENERATION);
    }

    public void next() {
        if (!hasNext()) {
            return;
        }

        UnifiedParser parser = pipeline.handle(sourceStream);

        if (parser == null) {
            throw new PandaParserException("Unrecognized syntax at line " + (sourceStream.read().getLine() + 1));
        }

        parser.parse(parserInfo);
        generation.executeImmediately(parserInfo);
    }

    public boolean hasNext() {
        return sourceStream.hasUnreadSource();
    }

}
