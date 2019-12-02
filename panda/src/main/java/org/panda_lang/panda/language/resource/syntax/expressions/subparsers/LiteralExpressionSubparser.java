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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.architecture.expression.ThisExpression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.interpreter.parser.expression.ExpressionParserUtils;
import org.panda_lang.framework.language.resource.internal.java.JavaModule;

public final class LiteralExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new SequenceWorker(context).withSubparser(this);
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String getSubparserName() {
        return "literal";
    }

    private static final class SequenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private final Prototype boolType;

        public SequenceWorker(Context context) {
            this.boolType = ModuleLoaderUtils.forClass(context, boolean.class);
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (token.getType() != TokenTypes.LITERAL) {
                return null;
            }

            switch (token.getValue()) {
                case "true":
                    return ExpressionParserUtils.toExpressionResult(boolType, true);
                case "false":
                    return ExpressionParserUtils.toExpressionResult(boolType, false);
                case "null":
                    return ExpressionParserUtils.toExpressionResult(null, null);
                case "this":
                    return ExpressionResult.of(ThisExpression.of(context.getContext()));
                default:
                    throw new PandaParserException("Unknown literal: " + token);
            }
        }

    }

}
