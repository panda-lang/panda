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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number;

import org.panda_lang.language.architecture.expression.DynamicExpression;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.language.runtime.ProcessStack;

import java.util.function.Function;

final class NotBitwiseExpression implements DynamicExpression {

    private final Expression numberValue;
    private final Function<Number, Number> notFunction;

    public NotBitwiseExpression(Expression numberValue, NumberType numberType) {
        this.numberValue = numberValue;
        this.notFunction = toFunction(numberType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Number evaluate(ProcessStack stack, Object instance) throws Exception {
        Number value = numberValue.evaluate(stack, instance);
        return notFunction.apply(value);
    }

    private Function<Number, Number> toFunction(NumberType numberType) {
        switch (numberType) {
            case BYTE:
                return value -> ~value.byteValue();
            case SHORT:
                return value -> ~value.shortValue();
            case INT:
                return value -> ~value.intValue();
            case LONG:
                return value -> ~value.longValue();
            default:
                throw new PandaRuntimeException("Bitwise not operator does not support " + numberType + " type");
        }
    }

    @Override
    public Signature getReturnType() {
        return numberValue.getSignature();
    }

}
