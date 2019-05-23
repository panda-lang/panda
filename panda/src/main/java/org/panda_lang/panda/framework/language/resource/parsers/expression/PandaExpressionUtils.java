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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionSubparsers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PandaExpressionUtils {

    public static ExpressionSubparsers collectSubparsers() {
        return collectSubparsers(PandaExpressions.SUBPARSERS);
    }

    @SafeVarargs
    public static ExpressionSubparsers collectSubparsers(Class<? extends ExpressionSubparser>... classes) {
        return new PandaExpressionSubparsers(Arrays.stream(classes)
                .map(clazz -> {
                    try {
                        return (ExpressionSubparser) clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
    }

}
