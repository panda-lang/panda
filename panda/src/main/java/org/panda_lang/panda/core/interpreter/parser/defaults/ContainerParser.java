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

package org.panda_lang.panda.core.interpreter.parser.defaults;

import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;

public class ContainerParser implements Parser {

    private final Container container;

    public ContainerParser(Container container) {
        this.container = container;
    }

    public void parse(ParserInfo info, TokenizedSource body) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        ParserPipelineRegistry parserPipelineRegistry = info.getComponent(Components.PIPELINE_REGISTRY);
        ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.SCOPE);

        SourceStream stream = new PandaSourceStream(body);
        info.setComponent(Components.SOURCE_STREAM, stream);

        Container previousContainer = info.getComponent("container");
        info.setComponent("container", container);

        while (stream.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(stream);

            if (parser == null) {
                throw new PandaParserException("Unrecognized syntax at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }

            int sourceLength = stream.getUnreadLength();

            parser.parse(info);
            generation.executeImmediately(info);
            info.setComponent("container", container);

            if (sourceLength == stream.getUnreadLength()) {
                throw new PandaParserException(parser.getClass().getSimpleName() + " did nothing with source at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }
        }

        info.setComponent("container", previousContainer);
    }

}
