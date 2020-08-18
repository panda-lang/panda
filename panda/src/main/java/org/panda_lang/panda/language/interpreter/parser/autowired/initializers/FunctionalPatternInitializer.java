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

package org.panda_lang.panda.language.interpreter.parser.autowired.initializers;

import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.pattern.Mappings;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalData;
import org.panda_lang.language.interpreter.pattern.functional.FunctionalPattern;
import org.panda_lang.language.interpreter.pattern.functional.PatternData;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.panda.language.interpreter.parser.autowired.IterationInitializer;

public final class FunctionalPatternInitializer implements IterationInitializer<FunctionalPattern> {

    private FunctionalPattern functionalPattern;

    public FunctionalPatternInitializer(FunctionalPattern functionalPattern) {
        this.functionalPattern = functionalPattern;
    }

    @Override
    public void handle(Context context, LocalChannel channel) {
        SourceStream source = context.getComponent(Components.STREAM);
        Snippet currentSource = source.toSnippet();
        channel.allocated("location", currentSource.getLocation());

        PatternData patternData = PatternData.capacity(1).with(FunctionalData.CONTEXT, context);
        Mappings mappings = functionalPattern.match(currentSource, source, patternData);

        if (!mappings.isMatched()) {
            throw new PandaParserFailure(context, currentSource, "CustomPatternInterceptor could not match pattern", "Make sure that the pattern does not have a typo");
        }

        channel.allocated("source", mappings.toSnippet());
        channel.allocated("result", mappings);
    }


}
