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

package org.panda_lang.panda.implementation.interpreter.parser;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.token.distributor.PandaSourceStream;

import java.util.Iterator;

public class OverallParser implements Iterator<Statement>, Iterable<Statement> {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final PandaSourceStream sourceStream;

    public OverallParser(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;
        this.pipeline = parserInfo.getComponent(Components.PARSER_PIPELINE);
        this.sourceStream = parserInfo.getComponent(Components.SOURCE_STREAM);
    }

    @Override
    public Statement next() {
        if (!hasNext()) {
            return null;
        }

        UnifiedParser parser = pipeline.handle(sourceStream);

        if (parser == null) {
            throw new PandaParserException("Unrecognized syntax at line " + (sourceStream.read().getLine() + 1));
        }

        Statement statement = parser.parse(parserInfo);

        if (statement == null) {
            throw new PandaParserException("Failed to parse statement at line " + (sourceStream.read().getLine() + 1));
        }

        return statement;
    }

    @NotNull
    @Override
    public Iterator<Statement> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return sourceStream.hasUnreadSource();
    }

}
