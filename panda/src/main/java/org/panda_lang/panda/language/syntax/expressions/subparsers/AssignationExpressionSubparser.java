/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.framework.architecture.dynamic.assigner.Assigner;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.architecture.statement.Variable;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.SourceStream;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.operator.Operators;

public final class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new AssignationWorker().withSubparser(this);
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.MUTUAL;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String name() {
        return "assignation";
    }

    private static final class AssignationWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!token.contentEquals(Operators.ASSIGNMENT)) {
                return null;
            }

            if (!(context.getResults().peek() instanceof AccessorExpression)) {
                return null;
            }

            AccessorExpression accessorExpression = (AccessorExpression) context.getResults().pop();
            Accessor<?> accessor = accessorExpression.getAccessor();
            Variable variable = accessor.getVariable();

            SourceStream valueSource = context.getSynchronizedSource().getAvailableSource().toStream();
            Expression value = context.getParser().parse(context, valueSource);
            context.getSynchronizedSource().next(valueSource.getReadLength());

            if (variable.awaitsSignature()) {
                variable.interfereSignature(value.getSignature());
            }
            else if (!accessor.getSignature().isAssignableFrom(value.getSignature())) {
                throw new PandaParserFailure(context.toContext(), token,
                        "Cannot assign " + value.getSignature() + " to " + accessor.getSignature(),
                        "Change variable type or ensure the expression has compatible return type"
                );
            }

            Expression equalizedExpression = ExpressionUtils.equalize(value, accessor.getSignature()).orElseThrow(error -> {
                throw new PandaParserFailure(context, "Incompatible signatures");
            });

            accessor.getVariable().initialize();
            Assigner<?> assigner = accessor.toAssigner(token, true, equalizedExpression);

            return ExpressionResult.of(assigner.toExpression());
        }

    }

}
