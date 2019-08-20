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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponents;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.PandaChannel;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.function.Supplier;

public final class PipelineParser<T extends ContextParser> implements Parser {

    private final ParserPipeline<T> pipeline;
    private final SourceStream stream;

    public PipelineParser(PipelineComponent<T> component, Context context) {
        this(context.getComponent(UniversalComponents.PIPELINE).getPipeline(component), context);
    }

    public PipelineParser(ParserPipeline<T> pipeline, Context context) {
        this(pipeline, context.getComponent(UniversalComponents.STREAM));
    }

    public PipelineParser(ParserPipeline<T> pipeline, SourceStream stream) {
        this.pipeline = pipeline;
        this.stream = stream;
    }

    /**
     * Parse source using the declared pipeline
     *
     * @param context the context to use
     * @param fork if true, context will be forked for each subparser
     * @return returns always null
     * @throws Exception if something happen in subparser
     */
    public @Nullable T parse(Context context, boolean fork) throws Exception {
        Interpretation interpretation = context.getComponent(UniversalComponents.INTERPRETATION);

        while (stream.hasUnreadSource() && interpretation.isHealthy()) {
            Channel channel = new PandaChannel();
            Snippet source = stream.toSnippet();
            HandleResult<T> result = pipeline.handle(context, channel, source);

            ContextParser parser = result.getParser().orElseThrow((Supplier<? extends Exception>) () -> {
                if (result.getFailure().isPresent()) {
                    throw result.getFailure().get();
                }

                throw PandaParserFailure.builder("Unrecognized syntax", context)
                        .withSource(stream.getOriginalSource(), source)
                        .build();
            });

            Context delegatedContext = (fork ? context.fork() : context)
                    .withComponent(PipelineComponents.CHANNEL, channel);

            int sourceLength = stream.getUnreadLength();
            parser.parse(delegatedContext);

            if (sourceLength == stream.getUnreadLength()) {
                throw PandaParserFailure.builder(parser.getClass().getSimpleName() + " did nothing with the current source", context)
                        .withSource(stream.getOriginalSource(), source)
                        .build();
            }

            if (stream.hasUnreadSource() && stream.getCurrent().contentEquals(Separators.SEMICOLON)) {
                stream.read();
            }
        }

        return null;
    }

}
