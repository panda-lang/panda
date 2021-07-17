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

package panda.interpreter.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.dynamic.accessor.Accessor;
import panda.interpreter.architecture.dynamic.accessor.AccessorExpression;
import panda.interpreter.architecture.dynamic.assigner.Assigner;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ExpressionUtils;
import panda.interpreter.architecture.statement.Variable;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionCategory;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserType;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.SourceStream;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.operator.Operators;

public final class AssignationPrarser implements ExpressionSubparser {

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
