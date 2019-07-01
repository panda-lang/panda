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

import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class OverallParser implements Parser {

    private final Interpretation interpretation;
    private final ParserPipeline<ContextParser> pipeline;
    private final SourceStream stream;

    public OverallParser(Context context) {
        this.interpretation = context.getComponent(UniversalComponents.INTERPRETATION);
        this.pipeline = context.getComponent(UniversalComponents.PIPELINE).getPipeline(UniversalPipelines.OVERALL);
        this.stream = context.getComponent(UniversalComponents.SOURCE_STREAM);
    }

    public void parseNext(Context context) throws Exception {
        if (!interpretation.isHealthy() || !hasNext()) {
            return;
        }

        Snippet source = stream.toSnippet();
        ContextParser parser = pipeline.handle(context, source);
        int sourceLength = stream.getUnreadLength();

        if (parser == null) {
            throw PandaParserFailure.builder("Unrecognized syntax", context)
                    .withSource(stream.getOriginalSource(), source)
                    .build();
        }

        parser.parse(context.fork());

        if (sourceLength == stream.getUnreadLength()) {
            throw PandaParserFailure.builder(parser.getClass().getSimpleName() + " did nothing with the current source", context)
                    .withSource(stream.getOriginalSource(), source)
                    .build();
        }

        if (stream.hasUnreadSource() && stream.getCurrent().contentEquals(Separators.SEMICOLON)) {
            stream.read();
        }
    }

    public boolean hasNext() {
        return stream.hasUnreadSource();
    }

}
