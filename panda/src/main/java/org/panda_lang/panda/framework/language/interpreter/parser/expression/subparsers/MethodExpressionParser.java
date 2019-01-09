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

package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.invoker.MethodInvokerExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.invoker.MethodInvokerExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

public class MethodExpressionParser implements ExpressionSubparser {

    private static final Token[] METHOD_SEPARATORS = ArrayUtils.of(Separators.PERIOD);
    private final boolean voids;

    public MethodExpressionParser(boolean voids) {
        this.voids = voids;
    }

    public MethodExpressionParser() {
        this(false);
    }

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readSeparated(main, source, METHOD_SEPARATORS, SubparserUtils.NAMES_FILTER, matchable -> {
            // at least 3 elements required: <method-name> ( )
            if ((matchable.getDistributor().size() - matchable.getIndex()) < 3) {
                return false;
            }

            // read dot
            matchable.nextVerified();

            // read method name
            matchable.nextVerified();

            if (!matchable.nextVerified().contentEquals(Separators.PARENTHESIS_LEFT)) {
                return false;
            }

            // parameters content
            while (matchable.hasNext() && !matchable.isMatchable()) {
                matchable.nextVerified();
            }

            if (!matchable.isMatchable()) {
               return false;
            }

            return matchable.current().contentEquals(Separators.PARENTHESIS_RIGHT);
        });

        // at least 3 elements required: <method-name> ( )
        if (selected == null || selected.size() < 3 ) {
            return null;
        }

        // method source has to end with parenthesis
        if (!selected.getLast().contentEquals(Separators.PARENTHESIS_RIGHT)) {
            return null;
        }

        // verify period-less structure
        if (!selected.contains(Separators.PERIOD) && !selected.get(1).contentEquals(Separators.PARENTHESIS_LEFT)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(source);

        if (methodInvokerParser == null) {
            return null;
        }

        methodInvokerParser.parse(source, data);
        MethodInvokerExpressionCallback callback = methodInvokerParser.toCallback();

        return new PandaExpression(callback.getReturnType(), callback);
    }

    @Override
    public int getMinimumLength() {
        return 3;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.METHOD;
    }

}
