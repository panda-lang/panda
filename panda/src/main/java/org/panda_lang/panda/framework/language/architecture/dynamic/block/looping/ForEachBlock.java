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

import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlowCallback;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractBlock;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

public class ForEachBlock extends AbstractBlock implements PandaControlFlowCallback {

    private final int variablePointer;
    private final ClassPrototype variableType;
    private final Expression expression;

    public ForEachBlock(int variablePointer, ClassPrototype variableType, Expression expression) {
        this.variablePointer = variablePointer;
        this.variableType = variableType;
        this.expression = expression;
    }

    @Override
    public void execute(Flow flow) {
        flow.callFlow(super.getStatementCells(), this);
    }

    @Override
    public void call(ControlFlow controlFlow, Flow flow) {
        ScopeFrame currentScope = flow.getCurrentScope();
        Value iterableValue = expression.evaluate(flow);

        for (Object value : (Iterable) iterableValue.getValue()) {
            currentScope.set(variablePointer, new PandaStaticValue(variableType, value));
            controlFlow.call();

            if (controlFlow.isEscaped() || flow.isInterrupted()) {
                break;
            }
        }
    }

}
