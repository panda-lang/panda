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

package org.panda_lang.panda.framework.language.resource.container.block.looping;

import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;

class ForEachBlock extends ControllerBlock {

    private final int variablePointer;
    private final Expression expression;

    ForEachBlock(int variablePointer, Expression expression) {
        this.variablePointer = variablePointer;
        this.expression = expression;
    }

    @Override
    public void call(ControlFlow controlFlow, Flow flow) {
        ScopeFrame currentScope = flow.getCurrentScope();
        Iterable iterableValue = expression.evaluate(flow);

        for (Object value : iterableValue) {
            currentScope.set(variablePointer, value);
            controlFlow.call();

            if (controlFlow.isEscaped() || flow.isInterrupted()) {
                break;
            }
        }
    }

}
