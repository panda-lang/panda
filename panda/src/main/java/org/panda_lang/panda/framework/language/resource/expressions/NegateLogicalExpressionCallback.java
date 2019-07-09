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

package org.panda_lang.panda.framework.language.resource.expressions;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

import java.security.InvalidParameterException;

public class NegateLogicalExpressionCallback implements ExpressionCallback {

    private final Expression logicalExpression;

    public NegateLogicalExpressionCallback(Expression logicalExpression) {
        if (!logicalExpression.getReturnType().isClassOf("Boolean")) {
            throw new InvalidParameterException("Cannot reverse non logical value");
        }

        this.logicalExpression = logicalExpression;
    }

    @Override
    public Value call(Expression expression, Flow flow) {
        Value value = logicalExpression.evaluate(flow);
        boolean val = value.getValue(); // TODO: Handle null?

        return new PandaStaticValue(expression.getReturnType(), !val);
    }

    @Override
    public ClassPrototype getReturnType() {
        return logicalExpression.getReturnType();
    }

}
