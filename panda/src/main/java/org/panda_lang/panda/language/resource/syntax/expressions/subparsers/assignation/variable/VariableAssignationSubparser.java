/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.panda_lang.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorScope;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.panda.language.interpreter.parser.PandaTargets;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationContext;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class VariableAssignationSubparser implements ContextParser<AssignationContext, Assigner<?>> {

    @Override
    public String name() {
        return "variable assignation";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(PandaTargets.ASSIGNATION);
    }

    @Override
    public double priority() {
        return AssignationPriorities.VARIABLE_ASSIGNATION;
    }

    @Override
    public Option<CompletableFuture<Assigner<?>>> parse(Context<? extends AssignationContext> context) {
        SourceStream stream = new PandaSourceStream(context.getSource());
        ExpressionTransaction transaction = context.getExpressionParser().parse(context, stream);

        if (stream.hasUnreadSource()) {
            transaction.rollback();
            return Option.none();
        }

        if (!(transaction.getExpression() instanceof AccessorExpression)) {
            throw new PandaParserFailure(context, context.getSource(), "Expression is not accessor");
        }

        AccessorExpression accessorExpression = (AccessorExpression) transaction.getExpression();
        boolean initialization = context.getScope().getFramedScope() instanceof ConstructorScope;
        Assigner<?> assigner = accessorExpression.getAccessor().toAssigner(context, initialization, accessorExpression);

        if (initialization) {
            assigner.getAccessor().getVariable().initialize();
        }

        return Option.of(CompletableFuture.completedFuture(assigner));
    }

}
