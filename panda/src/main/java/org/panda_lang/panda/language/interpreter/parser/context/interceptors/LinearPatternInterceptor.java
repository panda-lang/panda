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

package org.panda_lang.panda.language.interpreter.parser.context.interceptors;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapContent;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInterceptor;
import org.panda_lang.panda.language.interpreter.parser.context.data.InterceptorData;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.language.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.framework.language.interpreter.pattern.linear.LinearPatternResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

public class LinearPatternInterceptor implements BootstrapInterceptor {

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
    public InterceptorData handle(InterceptorData interceptorData, Context context) {
        if (pattern != null) {
            SourceStream stream = context.getComponent(Components.STREAM);
            Snippet currentSource = stream.toSnippet();

            ExpressionParser expressionParser = context.getComponent(Components.EXPRESSION);
            LinearPatternResult result = pattern.match(stream, source -> expressionParser.parse(context, source).getExpression());

            if (!result.isMatched()) {
                throw new PandaParserFailure(context, currentSource, "Interceptor could not match pattern '" + content.getPattern().orElse("<pattern is null>") + "'");
            }

            interceptorData.addElement(currentSource.getLocation());
            interceptorData.addElement(result.getSource());
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
