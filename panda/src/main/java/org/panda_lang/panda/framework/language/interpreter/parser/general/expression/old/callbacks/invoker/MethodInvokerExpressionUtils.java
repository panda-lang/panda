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

package org.panda_lang.panda.framework.language.interpreter.parser.general.expression.old.callbacks.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.mapping.AbyssPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.abyss.mapping.AbyssPatternMappingHollows;

import java.util.List;

public class MethodInvokerExpressionUtils {

    public static @Nullable MethodInvokerExpressionParser match(Tokens source) {
        List<Tokens> methodMatches = MethodInvokerExpressionParser.PATTERN.match(source);

        if (methodMatches == null || methodMatches.size() == 0) {
            return null;
        }

        AbyssPatternMappingHollows hollows = new AbyssPatternMappingHollows(methodMatches);
        AbyssPatternMapping redactor = new AbyssPatternMapping(hollows);
        redactor.map("method-call", "arguments");

        Tokens methodCallSource = redactor.get("method-call");
        List<Tokens> methodCallMatches = MethodInvokerExpressionParser.CALL_PATTERN.match(methodCallSource);

        Tokens argumentsSource = redactor.get("arguments");
        Tokens methodNameSource = methodCallSource;
        Tokens instanceSource = null;

        if (methodCallMatches != null && methodCallMatches.size() > 0) {
            AbyssPatternMappingHollows methodCallHollows = new AbyssPatternMappingHollows(methodCallMatches);
            AbyssPatternMapping methodCallRedactor = new AbyssPatternMapping(methodCallHollows);

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
