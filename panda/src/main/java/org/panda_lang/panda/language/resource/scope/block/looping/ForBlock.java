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

package org.panda_lang.panda.language.resource.scope.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.language.interpreter.parser.expression.ExpressionUtils;

class ForBlock extends ControllerBlock {

    private final Expression conditionExpression;
    private final @Nullable Expression initializationStatement;
    private final @Nullable Expression postExpression;

    ForBlock(Scope parent, @Nullable Expression initializationStatement, Expression conditionExpression, @Nullable Expression postExpression) {
        super(parent);
        this.initializationStatement = initializationStatement;
        this.conditionExpression = conditionExpression;
        this.postExpression = postExpression;
    }

    @Override
    public void call(ControlFlow controlFlow, Flow flow) {
        if (initializationStatement != null) {
            initializationStatement.evaluate(flow);
        }

        for (; conditionExpression.evaluate(flow); ExpressionUtils.evaluate(flow, postExpression)) {
            controlFlow.call();

            if (controlFlow.isEscaped() || flow.isInterrupted()) {
                break;
            }
        }
    }

}
