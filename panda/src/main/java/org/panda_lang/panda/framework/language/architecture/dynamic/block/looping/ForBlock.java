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

package org.panda_lang.panda.framework.language.architecture.dynamic.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlowCallback;

public class ForBlock extends AbstractBlock implements PandaControlFlowCallback {

    private final Expression conditionExpression;
    private final @Nullable Executable initializationStatement;
    private final @Nullable Expression postExpression;

    public ForBlock(@Nullable Executable initializationStatement, Expression conditionExpression, @Nullable Expression postExpression) {
        this.initializationStatement = initializationStatement;
        this.conditionExpression = conditionExpression;
        this.postExpression = postExpression;
    }

    @Override
    public void execute(Flow flow) {
        flow.callFlow(super.getStatementCells(), this);
    }

    @Override
    public void call(ControlFlow controlFlow, Flow flow) {
        if (initializationStatement != null) {
            initializationStatement.execute(flow);
        }

        for (; conditionExpression.evaluate(flow).getValue(); ExpressionUtils.evaluate(flow, postExpression)) {
            controlFlow.call();

            if (controlFlow.isEscaped() || flow.isInterrupted()) {
                break;
            }
        }
    }

}
