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

import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.language.interpreter.pattern.linear.LinearPatternResult;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapContent;
import org.panda_lang.panda.language.interpreter.parser.context.IterationInitializer;

public final class LinearPatternInitializer implements IterationInitializer {

    private BootstrapContent content;
    private LinearPattern pattern;

    @Override
    public void initialize(BootstrapContent content) {
        this.content = content;

        if (!content.getPattern().isPresent()) {
            return;
        }

        this.pattern = LinearPattern.compile(content.getPattern().get().toString());
    }

    @Override
    public void handle(Context context, LocalChannel channel) {
        if (pattern == null) {
            return;
        }

        SourceStream stream = context.getComponent(Components.STREAM);
        Snippet currentSource = stream.toSnippet();
        channel.override("location", currentSource.getLocation());

        ExpressionParser expressionParser = context.getComponent(Components.EXPRESSION);
        LinearPatternResult result = pattern.match(stream, source -> expressionParser.parse(context, source).getExpression());

        if (!result.isMatched()) {
            throw new PandaParserFailure(context, currentSource, "Interceptor could not match pattern '" + content.getPattern().orElseGet("<pattern is null>") + "'");
        }

        channel.override("source", result.getSource());
        channel.override("result", result);
    }

}
