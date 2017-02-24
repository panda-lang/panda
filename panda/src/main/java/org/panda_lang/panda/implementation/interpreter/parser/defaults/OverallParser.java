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

package org.panda_lang.panda.implementation.interpreter.parser.defaults;

import org.panda_lang.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;

public class OverallParser {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final SourceStream sourceStream;
    private final ParserGeneration generation;

    public OverallParser(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;
        this.pipeline = parserInfo.getComponent(Components.PARSER_PIPELINE);
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
