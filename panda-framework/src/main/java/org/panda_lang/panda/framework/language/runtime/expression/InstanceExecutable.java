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

package org.panda_lang.panda.framework.language.runtime.expression;

import org.panda_lang.panda.framework.design.architecture.dynamic.StandaloneExecutable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.runtime.PandaFlow;

public class InstanceExecutable extends AbstractExecutableStatement implements StandaloneExecutable, ExpressionCallback {

    private final Expression instanceExpression;
    private final Expression expression;

    public InstanceExecutable(Expression instance, Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }

        this.instanceExpression = instance;
        this.expression = expression;
    }

    @Override
    public Value call(Expression expression, Flow flow) {
        execute(flow);
        return flow.getReturnedValue();
    }

    @Override
    public void execute(Flow flow) {
        /*
        if (instanceExpression != null) {
            Value instance = instanceExpression.evaluate(frame);

            // exclude static and null values
            if (instance != null && !instance.isNull()) {
                frame.instance(instance);
            }
        }
        */

        Value instance = instanceExpression.evaluate(flow);

        if (instance == null) {
            instance = PandaStaticValue.NULL;
        }

        Flow subFlow = new PandaFlow(flow, instance);
        expression.evaluate(subFlow);
    }

    @Override
    public ClassPrototype getReturnType() {
        return expression.getReturnType();
    }

}
