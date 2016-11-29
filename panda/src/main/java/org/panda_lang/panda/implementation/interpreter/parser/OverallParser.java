/*
 * Copyright (c) 2015-2016 Dzikoysk
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

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Statement;

import java.util.Iterator;

public class OverallParser implements Iterator<Statement>, Iterable<Statement> {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final TokenReader tokenReader;

    public OverallParser(ParserInfo parserInfo, TokenReader tokenReader) {
        this.parserInfo = parserInfo;
        this.pipeline = parserInfo.getComponent(Components.PARSER_PIPELINE);
        this.tokenReader = tokenReader;
    }

    @Override
    public Statement next() {
        ParserInfo modifiedParserInfo = parserInfo.clone();
        modifiedParserInfo.setComponent(Components.READER, tokenReader);

        while (tokenReader.hasNext()) {
            tokenReader.synchronize();

            UnifiedParser parser = pipeline.handle(tokenReader);

            if (parser == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Unrecognized " + tokenReader.read());
            }

            Statement statement = parser.parse(modifiedParserInfo);

            if (statement == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Failed to parse statement at line " + (tokenReader.previous().getLine() + 1));
            }

            return statement;
        }

        return null;
    }

    @Override
    public Iterator<Statement> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return tokenReader.hasNext();
    }

}
