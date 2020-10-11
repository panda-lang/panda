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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.ControlledScope;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.AbstractBlock;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Result;

final class ForBlock extends AbstractBlock implements ControlledScope {

    private final Expression conditionExpression;
    private final @Nullable Expression initializationStatement;
    private final @Nullable Expression postExpression;

    ForBlock(Scope parent, Localizable localizable, @Nullable Expression initializationStatement, Expression conditionExpression, @Nullable Expression postExpression) {
        super(parent, localizable);
        this.initializationStatement = initializationStatement;
        this.conditionExpression = conditionExpression;
        this.postExpression = postExpression;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        if (initializationStatement != null) {
            initializationStatement.evaluate(stack, instance);
        }

        return new ControlledIteration(
                () -> conditionExpression.evaluate(stack, instance),
                () -> evaluate(stack, instance, postExpression)
        ).iterate(stack, instance, this);
    }

    private @Nullable Object evaluate(ProcessStack stack, Object instance, @Nullable Expression expression) throws Exception {
        return expression != null ? expression.evaluate(stack, instance) : null;
    }

}
