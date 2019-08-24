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

package org.panda_lang.panda.framework.language.resource.scope.block.looping;

import org.panda_lang.panda.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;

class ForEachBlock extends ControllerBlock {

    private final int valuePointer;
    private final Expression iterableExpression;

    ForEachBlock(Scope parent, Expression iterableExpression) {
        super(parent);
        this.iterableExpression = iterableExpression;
        this.valuePointer = getFrame().allocate();
    }

    @Override
    public void call(ControlFlow controlFlow, Flow flow) {
        LivingFrame scope = flow.getCurrentScope();
        Iterable iterable = iterableExpression.evaluate(flow);

        for (Object value : iterable) {
            scope.set(valuePointer, value);
            controlFlow.call();

            if (controlFlow.isEscaped() || flow.isInterrupted()) {
                break;
            }
        }
    }

    public Expression getIterableExpression() {
        return iterableExpression;
    }

    public int getValuePointer() {
        return valuePointer;
    }
    
}
