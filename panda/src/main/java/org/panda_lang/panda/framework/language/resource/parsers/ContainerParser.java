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

package org.panda_lang.panda.framework.language.resource.parsers;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public class ContainerParser implements Parser {

    private final Container container;

    public ContainerParser(Container container) {
        this.container = container;
    }

    public void parse(ParserData data, Snippet body) throws Throwable {
        ParserData delegatedData = data.fork();

        Generation generation = delegatedData.getComponent(UniversalComponents.GENERATION);
        PipelinePath pipelinePath = delegatedData.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline<UnifiedParser> pipeline = pipelinePath.getPipeline(PandaPipelines.SCOPE);

        SourceStream source = new PandaSourceStream(body);
        delegatedData.setComponent(UniversalComponents.SOURCE_STREAM, source);

        Container previousContainer = delegatedData.getComponent(PandaComponents.CONTAINER);
        delegatedData.setComponent(PandaComponents.CONTAINER, container);

        while (source.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(delegatedData, source.toSnippet());
            int sourceLength = source.getUnreadLength();

            if (parser == null) {
                throw new PandaParserFailure("Unrecognized syntax", data, source.toSnippet());
            }

            try {
                parser.parse(delegatedData);
            }
            catch (ParserFailure failure) {
                failure.getData().setComponent(UniversalComponents.SOURCE_STREAM, source);
                throw failure;
            }

            if (sourceLength == source.getUnreadLength()) {
                throw new PandaParserFailure(parser.getClass().getSimpleName() + " did nothing with source", delegatedData, source.toSnippet());
            }

            delegatedData.setComponent(PandaComponents.CONTAINER, container);
        }

        delegatedData.setComponent(PandaComponents.CONTAINER, previousContainer);
    }

}
