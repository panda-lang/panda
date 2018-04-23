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

import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;

public class OverallParser implements Parser {

    private final ParserPipeline pipeline;
    private final SourceStream stream;
    private final CasualParserGeneration generation;

    public OverallParser(ParserPipeline pipeline, CasualParserGeneration generation, SourceStream stream) {
        this.pipeline = pipeline;
        this.generation = generation;
        this.stream = stream;
    }

    public void next(ParserData data) {
        if (!hasNext()) {
            return;
        }

        UnifiedParser parser = pipeline.handle(stream);

        if (parser == null) {
            InterpreterFailure failure = new PandaInterpreterFailure("Unrecognized syntax at line {line}", data);
            data.setComponent(UniversalComponents.FAILURE, failure);
            return;
            // throw new PandaParserException("Unrecognized syntax at line " + TokenUtils.getLine(stream.toTokenizedSource()));
        }

        int sourceLength = stream.getUnreadLength();

        parser.parse(data);
        generation.executeImmediately(data);

        if (sourceLength == stream.getUnreadLength()) {
            throw new PandaParserException(parser.getClass().getSimpleName() + " did nothing with source at line " + TokenUtils.getLine(stream.toTokenizedSource()));
        }
    }

    public boolean hasNext() {
        return stream.hasUnreadSource();
    }

}
