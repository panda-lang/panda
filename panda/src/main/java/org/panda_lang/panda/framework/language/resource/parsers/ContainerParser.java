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
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ContainerParser implements Parser {

    public Container parse(Container container, Snippet body, Context context) throws Exception {
        Context delegatedContext = context.fork();
        PipelinePath pipelinePath = delegatedContext.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline<ContextParser> pipeline = pipelinePath.getPipeline(PandaPipelines.CONTAINER);

        SourceStream source = new PandaSourceStream(body);
        delegatedContext.withComponent(UniversalComponents.SOURCE_STREAM, source);

        Container previousContainer = delegatedContext.getComponent(PandaComponents.CONTAINER);
        delegatedContext.withComponent(PandaComponents.CONTAINER, container);

        while (source.hasUnreadSource()) {
            Snippet currentSource = source.toSnippet();
            ContextParser parser = pipeline.handle(delegatedContext.fork(), source.toSnippet());
            int sourceLength = source.getUnreadLength();

            if (parser == null) {
                throw PandaParserFailure.builder("Unrecognized syntax", context)
                        .withSource(body, currentSource)
                        .build();
            }

            parser.parse(delegatedContext);

            if (sourceLength == source.getUnreadLength()) {
                throw PandaParserFailure.builder(parser.getClass().getSimpleName() + " did nothing with source", delegatedContext)
                        .withSource(body, currentSource)
                        .build();
            }

            delegatedContext.withComponent(PandaComponents.CONTAINER, container);

            if (source.hasUnreadSource() && source.getCurrent().contentEquals(Separators.SEMICOLON)) {
                source.read();
            }
        }

        delegatedContext.withComponent(PandaComponents.CONTAINER, previousContainer);
        return container;
    }

}
