/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.design.interpreter.parser.defaults;

import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;

public class OverallParser implements Parser {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final SourceStream stream;
    private final CasualParserGeneration generation;

    public OverallParser(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;

        ParserPipelineRegistry parserPipelineRegistry = parserInfo.getComponent(PandaComponents.PIPELINE_REGISTRY);
        this.pipeline = parserPipelineRegistry.getPipeline(PandaPipelines.OVERALL);

        this.stream = parserInfo.getComponent(PandaComponents.SOURCE_STREAM);
        this.generation = parserInfo.getComponent(PandaComponents.GENERATION);
    }

    public void next() {
        if (!hasNext()) {
            return;
        }

        UnifiedParser parser = pipeline.handle(stream);

        if (parser == null) {
            throw new PandaParserException("Unrecognized syntax at line " + TokenUtils.getLine(stream.toTokenizedSource()));
        }

        int sourceLength = stream.getUnreadLength();

        parser.parse(parserInfo);
        generation.executeImmediately(parserInfo);

        if (sourceLength == stream.getUnreadLength()) {
            throw new PandaParserException(parser.getClass().getSimpleName() + " did nothing with source at line " + TokenUtils.getLine(stream.toTokenizedSource()));
        }
    }

    public boolean hasNext() {
        return stream.hasUnreadSource();
    }

}
