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

package org.panda_lang.panda.framework.language.interpreter.parser.defaults;

import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class OverallParser implements Parser {

    private final Interpretation interpretation;
    private final ParserPipeline<UnifiedParser> pipeline;
    private final SourceStream stream;

    public OverallParser(ParserData data) {
        this.interpretation = data.getComponent(UniversalComponents.INTERPRETATION);
        this.pipeline = data.getComponent(UniversalComponents.PIPELINE).getPipeline(UniversalPipelines.OVERALL);
        this.stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
    }

    public void parseNext(ParserData data) throws Throwable {
        if (!interpretation.isHealthy() || !hasNext()) {
            return;
        }

        UnifiedParser parser = pipeline.handle(stream);
        int sourceLength = stream.getUnreadLength();

        if (parser == null) {
            stream.updateCachedSource();
            throw new PandaParserFailure("Unrecognized syntax", data);
        }

        parser.parse(data.fork());

        if (sourceLength == stream.getUnreadLength()) {
            throw new PandaParserFailure(parser.getClass().getSimpleName() + " did nothing with the current source", data);
        }
    }

    public boolean hasNext() {
        return stream.hasUnreadSource();
    }

}
