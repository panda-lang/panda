/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.context.initializers;

import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternData;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.UniversalData;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapContent;
import org.panda_lang.panda.language.interpreter.parser.context.IterationInitializer;

import java.util.function.Supplier;

public final class CustomPatternInitializer implements IterationInitializer {

    private CustomPattern pattern;

    @Override
    public void initialize(BootstrapContent content) {
        this.pattern = (CustomPattern) content.getPattern().orThrow((Supplier<? extends PandaParserFailure>) () -> {
            throw new PandaParserFailure(content.getContext(), "Missing pattern");
        });
    }

    @Override
    public void handle(Context context, LocalChannel channel) {
        SourceStream source = context.getComponent(Components.STREAM);
        Snippet currentSource = source.toSnippet();
        channel.allocated("location", currentSource.getLocation());

        CustomPatternData patternData = new CustomPatternData().with(UniversalData.CONTEXT, context);
        Result result = pattern.match(currentSource, source, patternData);

        if (!result.isMatched()) {
            throw new PandaParserFailure(context, currentSource, "CustomPatternInterceptor could not match pattern", "Make sure that the pattern does not have a typo");
        }

        channel.allocated("source", result.getSource());
        channel.allocated("result", result);
    }

}
