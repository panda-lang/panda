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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapCoreParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.linear.LinearPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.linear.LinearPatternResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSourceFragment;

public class LinearPatternInterceptor implements BootstrapInterceptor {

    private LinearPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap, ParserData data) {
        this.pattern = LinearPattern.compile(bootstrap.getPattern().toString());
    }

    @Override
    public InterceptorData handle(BootstrapCoreParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
            Snippet currentSource = stream.toSnippet();

            ExpressionParser expressionParser = data.getComponent(UniversalComponents.EXPRESSION);
            LinearPatternResult result = pattern.match(stream, source -> expressionParser.parse(data, source));

            if (!result.isMatched()) {
                throw PandaParserFailure.builder("Interceptor could not match token pattern", data)
                        .withSourceFragment(new PandaSourceFragment(currentSource))
                        .build();
            }

            interceptorData.addElement(new LinearPatternMapping(result));
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
