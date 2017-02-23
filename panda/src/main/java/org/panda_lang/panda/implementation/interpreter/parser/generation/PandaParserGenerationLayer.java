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

package org.panda_lang.panda.implementation.interpreter.parser.generation;

import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;

import java.util.ArrayList;
import java.util.List;

public class PandaParserGenerationLayer implements ParserGenerationLayer {

    private final List<ParserGenerationCallback> before;
    private final List<ParserGenerationCallback> delegates;
    private final List<ParserGenerationCallback> after;

    public PandaParserGenerationLayer() {
        this.before = new ArrayList<>();
        this.delegates = new ArrayList<>();
        this.after = new ArrayList<>();
    }

    @Override
    public void call(ParserInfo parserInfo) {
        for (ParserGenerationCallback callback : before) {
            callback.call(parserInfo);
        }

        for (ParserGenerationCallback callback : delegates) {
            callback.call(parserInfo);
        }

        for (ParserGenerationCallback callback : after) {
            callback.call(parserInfo);
        }
    }

    @Override
    public PandaParserGenerationLayer delegateBefore(ParserGenerationCallback callback) {
        this.before.add(callback);
        return this;
    }

    @Override
    public PandaParserGenerationLayer delegate(ParserGenerationCallback callback) {
        this.delegates.add(callback);
        return this;
    }

    @Override
    public PandaParserGenerationLayer delegateAfter(ParserGenerationCallback callback) {
        this.after.add(callback);
        return this;
    }

}
