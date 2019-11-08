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

package org.panda_lang.panda.language.interpreter.parser.context.handlers;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternData;
import org.panda_lang.framework.language.interpreter.pattern.custom.UniversalData;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapContent;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapHandler;

import java.util.function.Supplier;

public final class CustomPatternHandler implements BootstrapHandler {

    private CustomPattern pattern;

    @Override
    public void initialize(BootstrapContent content) {
        this.pattern = (CustomPattern) content.getPattern().orElseThrow((Supplier<? extends PandaParserFailure>) () -> {
            throw new PandaParserFailure(content.getContext(), "Cannot initialize custom pattern handler - pattern is null");
        });
    }

    @Override
    public Object handle(Context context, Channel channel, Snippet source) {
        return pattern
                .match(new PandaSourceStream(source), new CustomPatternData().with(UniversalData.CONTEXT, context))
                .isMatched();
    }

}
