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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping.GappedPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.mapping.GappedPatternMappingContent;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.List;

public class MethodInvokerExpressionUtils {

    protected static final GappedPattern PATTERN = new GappedPatternBuilder()
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .lastIndexAlgorithm(true)
            .build();

    protected static final GappedPattern CALL_PATTERN = new GappedPatternBuilder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .lastIndexAlgorithm(true)
            .build();

    public static @Nullable MethodInvokerExpressionParser match(Snippet source) {
        List<Snippet> methodMatches = PATTERN.match(source);

        if (methodMatches == null || methodMatches.size() == 0) {
            return null;
        }

        GappedPatternMappingContent hollows = new GappedPatternMappingContent(methodMatches);
        GappedPatternMapping redactor = new GappedPatternMapping(hollows);
        redactor.map("method-call", "arguments");

        Snippet methodCallSource = redactor.get("method-call");
        List<Snippet> methodCallMatches = CALL_PATTERN.match(methodCallSource);

        Snippet argumentsSource = redactor.get("arguments");
        Snippet methodNameSource = methodCallSource;
        Snippet instanceSource = null;

        if (methodCallMatches != null && methodCallMatches.size() > 0) {
            GappedPatternMappingContent methodCallHollows = new GappedPatternMappingContent(methodCallMatches);
            GappedPatternMapping methodCallRedactor = new GappedPatternMapping(methodCallHollows);

            methodCallRedactor.map("instance", "method-name");
            instanceSource = methodCallRedactor.get("instance");
            methodNameSource = methodCallRedactor.get("method-name");
        }
        else if (methodCallSource == null || methodCallSource.size() > 1) {
            return null;
        }

        return new MethodInvokerExpressionParser(instanceSource, methodNameSource, argumentsSource);
    }

}
