/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberType;

final class NegativeExpression implements DynamicExpression {

    private final Expression logicalExpression;
    private final NumberType numberType;

    public NegativeExpression(Expression logicalExpression, NumberType numberType) {
        this.logicalExpression = logicalExpression;
        this.numberType = numberType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Number evaluate(ProcessStack stack, Object instance) throws Exception {
        Number value = logicalExpression.evaluate(stack, instance);

        switch (numberType) {
            case BYTE:
                return -value.byteValue();
            case SHORT:
                return -value.shortValue();
            case INT:
                return -value.intValue();
            case LONG:
                return -value.longValue();
            case FLOAT:
                return -value.floatValue();
            case DOUBLE:
                return -value.doubleValue();
            default:
                throw new PandaRuntimeException("Unsupported number type " + numberType);
        }
    }

    @Override
    public Type getReturnType() {
        return logicalExpression.getType();
    }

}
