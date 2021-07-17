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
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ThisExpression;
import panda.interpreter.architecture.statement.Variable;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.VisibilityComparator;
import panda.interpreter.architecture.type.member.field.TypeField;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.token.TokenUtils;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.expressions.subparsers.number.NumberUtils;
import panda.std.Option;

public final class VariableParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new VariableWorker().withSubparser(this);
    }

    @Override
    public String name() {
        return "variable";
    }

    private static final class VariableWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            boolean period = TokenUtils.contentEquals(context.getSynchronizedSource().getPrevious(), Separators.PERIOD);

            if (token.getType() != TokenTypes.UNKNOWN) {
                return null;
            }

            if ((!period && context.hasResults()) || (period && !context.hasResults())) {
                return null;
            }

            // do not accept variable names starting with numbers
            if (NumberUtils.startsWithNumber(token.getToken())) {
                return null;
            }

            String name = token.getValue();

            // if there is anything on stack, we can search only for fields
            if (context.hasResults()) {
                ExpressionResult result = fromInstance(context, context.peekExpression(), token).orElseGet(() -> {
                    return ExpressionResult.error("Cannot find field called '" + name + "'", token);
                });

                if (result.isPresent()) {
                    context.popExpression();
                }

                return result;
            }

            Option<Variable> variableValue = context.toContext().getScope().getVariable(name);

            // respect local variables before fields
            if (variableValue.isDefined()) {
                Variable variable = variableValue.get();
                return ExpressionResult.of(new VariableExpression(variable).toExpression());
            }

            if (context.toContext().getSubject() instanceof TypeContext) {
                return fromInstance(context, ThisExpression.ofUnknownContext(context.toContext()), token).orElseGet(() -> {
                    return ExpressionResult.error("Cannot find class/variable '" + name + "'", token);
                });
            }

            // return ExpressionResult.error("Cannot find variable or field called '" + name + "'", token);
            return null;
        }

        private Option<ExpressionResult> fromInstance(ExpressionContext<?> context, Expression instance, TokenInfo name) {
            Option<TypeField> fieldValue = instance.getKnownType().getFields().getField(name.getValue());

            if (fieldValue.isDefined()) {
                Option<String> issue = VisibilityComparator.canAccess(fieldValue.get(), context.toContext());

                if (issue.isDefined()) {
                    throw new PandaParserFailure(context.toContext(), name, issue.get(), VisibilityComparator.NOTE_MESSAGE);
                }
            }

            return fieldValue.map(property -> ExpressionResult.of(new FieldExpression(instance, property).toExpression()));
        }

    }

}
