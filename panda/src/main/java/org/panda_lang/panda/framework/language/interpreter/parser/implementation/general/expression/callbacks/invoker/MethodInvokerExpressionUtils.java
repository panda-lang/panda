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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.callbacks.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;

import java.util.List;

public class MethodInvokerExpressionUtils {

    public static @Nullable MethodInvokerExpressionParser match(TokenizedSource source) {
        List<TokenizedSource> methodMatches = MethodInvokerExpressionParser.PATTERN.match(source);

        if (methodMatches == null || methodMatches.size() == 0) {
            return null;
        }

        AbyssRedactorHollows hollows = new AbyssRedactorHollows(methodMatches);
        AbyssRedactor redactor = new AbyssRedactor(hollows);
        redactor.map("method-call", "arguments");

        TokenizedSource methodCallSource = redactor.get("method-call");
        List<TokenizedSource> methodCallMatches = MethodInvokerExpressionParser.CALL_PATTERN.match(methodCallSource);

        TokenizedSource argumentsSource = redactor.get("arguments");
        TokenizedSource methodNameSource = methodCallSource;
        TokenizedSource instanceSource = null;

        if (methodCallMatches != null && methodCallMatches.size() > 0) {
            AbyssRedactorHollows methodCallHollows = new AbyssRedactorHollows(methodCallMatches);
            AbyssRedactor methodCallRedactor = new AbyssRedactor(methodCallHollows);

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
