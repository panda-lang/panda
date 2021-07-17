/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers.number;

import panda.interpreter.architecture.expression.DynamicExpression;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.interpreter.runtime.ProcessStack;

import java.util.function.Function;

final class NegativeExpression implements DynamicExpression {

    private final Expression logicalExpression;
    private final Function<Number, Number> negativeFunction;

    public NegativeExpression(Expression logicalExpression, NumberType numberType) {
        this.logicalExpression = logicalExpression;
        this.negativeFunction = toFunction(numberType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Number evaluate(ProcessStack stack, Object instance) throws Exception {
        Number value = logicalExpression.evaluate(stack, instance);
        return negativeFunction.apply(value);
    }

    private Function<Number, Number> toFunction(NumberType numberType) {
        switch (numberType) {
            case BYTE:
                return value -> -value.byteValue();
            case SHORT:
                return value -> -value.shortValue();
            case INT:
                return value -> -value.intValue();
            case LONG:
                return value -> -value.longValue();
            case FLOAT:
                return value -> -value.floatValue();
            case DOUBLE:
                return value -> -value.doubleValue();
            default:
                throw new PandaRuntimeException("Unsupported number type " + numberType);
        }
    }

    @Override
    public Signature getReturnType() {
        return logicalExpression.getSignature();
    }

}
