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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public class ContainerParser implements Parser {

    private final Container container;

    public ContainerParser(Container container) {
        this.container = container;
    }

    public void parse(ParserData data, TokenizedSource body) throws Throwable {
        ParserData delegatedData = data.fork();

        Generation generation = delegatedData.getComponent(UniversalComponents.GENERATION);
        PipelineRegistry pipelineRegistry = delegatedData.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.SCOPE);

        SourceStream stream = new PandaSourceStream(body);
        delegatedData.setComponent(UniversalComponents.SOURCE_STREAM, stream);

        Container previousContainer = delegatedData.getComponent(PandaComponents.CONTAINER);
        delegatedData.setComponent(PandaComponents.CONTAINER, container);

        while (stream.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(stream);
            int sourceLength = stream.getUnreadLength();

            if (parser == null) {
                throw new PandaParserFailure("Unrecognized syntax", data);
            }

            parser.parse(delegatedData);

            if (sourceLength == stream.getUnreadLength()) {
                throw new PandaParserFailure(parser.getClass().getSimpleName() + " did nothing with source", delegatedData);
            }

            delegatedData.setComponent(PandaComponents.CONTAINER, container);
        }

        delegatedData.setComponent(PandaComponents.CONTAINER, previousContainer);
    }

}
