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

package org.panda_lang.panda.framework.language.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;

public class PandaParserRepresentation implements ParserRepresentation {

    private final UnifiedParser parser;
    private final ParserHandler handler;
    private final int priority;
    private int usages;

    public PandaParserRepresentation(UnifiedParser parser, ParserHandler parserHandler, int priority) {
        this.parser = parser;
        this.handler = parserHandler;
        this.priority = priority;
    }

    @Override
    public void increaseUsages() {
        usages++;
    }

    @Override
    public int getUsages() {
        return usages;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public ParserHandler getHandler() {
        return handler;
    }

    @Override
    public UnifiedParser getParser() {
        return parser;
    }

}