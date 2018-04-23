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

import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;

public class ContainerParser implements Parser {

    private final Container container;

    public ContainerParser(Container container) {
        this.container = container;
    }

    public void parse(ParserData info, TokenizedSource body) {
        CasualParserGeneration generation = info.getComponent(UniversalComponents.GENERATION);

        PipelineRegistry pipelineRegistry = info.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.SCOPE);

        SourceStream stream = new PandaSourceStream(body);
        info.setComponent(UniversalComponents.SOURCE_STREAM, stream);

        Container previousContainer = info.getComponent(PandaComponents.CONTAINER);
        info.setComponent(PandaComponents.CONTAINER, container);

        while (stream.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(stream);

            if (parser == null) {
                throw new PandaParserException("Unrecognized syntax at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }

            int sourceLength = stream.getUnreadLength();

            parser.parse(info);
            generation.executeImmediately(info);
            info.setComponent(PandaComponents.CONTAINER, container);

            if (sourceLength == stream.getUnreadLength()) {
                throw new PandaParserException(parser.getClass().getSimpleName() + " did nothing with source at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }
        }

        info.setComponent(PandaComponents.CONTAINER, previousContainer);
    }

}
