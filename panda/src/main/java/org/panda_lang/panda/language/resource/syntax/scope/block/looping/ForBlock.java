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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.runtime.Status;
import org.panda_lang.framework.language.architecture.statement.AbstractBlock;

final class ForBlock extends AbstractBlock implements ControlledScope {

    private final Expression conditionExpression;
    private final @Nullable Expression initializationStatement;
    private final @Nullable Expression postExpression;

    ForBlock(Scope parent, SourceLocation location, @Nullable Expression initializationStatement, Expression conditionExpression, @Nullable Expression postExpression) {
        super(parent, location);
        this.initializationStatement = initializationStatement;
        this.conditionExpression = conditionExpression;
        this.postExpression = postExpression;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        if (initializationStatement != null) {
            initializationStatement.evaluate(stack, instance);
        }

        for (; conditionExpression.evaluate(stack, instance); evaluate(stack, instance, postExpression)) {
            Result<?> result = stack.call(instance, this);

            if (result == null || result.getStatus() == Status.CONTINUE) {
                continue;
            }

            if (result.getStatus() == Status.BREAK) {
                break;
            }

            return result;
        }

        return null;
    }

    private @Nullable Object evaluate(ProcessStack stack, Object instance, @Nullable Expression expression) throws Exception {
        return expression != null ? expression.evaluate(stack, instance) : null;
    }

}
