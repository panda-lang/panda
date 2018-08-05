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

package org.panda_lang.panda.framework.language.parser.bootstrap;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;

public class PandaParserBootstrap {

    private String pipeline;
    private ParserHandler handler;
    private String pattern;
    private String[] wildcardNames;

    public PandaParserBootstrap pipeline(String pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public PandaParserBootstrap handler(ParserHandler handler) {
        this.handler = handler;
        return this;
    }

    public PandaParserBootstrap pattern(String pattern, String... wildcardNames) {
        this.pattern = pattern;
        this.wildcardNames = wildcardNames;
        return this;
    }

    public PandaParserBuilder parser() {
        return new PandaParserBuilder(this);
    }

    @Nullable
    public ParserRepresentation build() {
        return null;
    }

}
