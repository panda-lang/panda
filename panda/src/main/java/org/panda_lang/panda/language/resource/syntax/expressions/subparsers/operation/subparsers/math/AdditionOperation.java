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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.math;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;

import java.util.function.BiFunction;

public final class AdditionOperation extends MathOperation {

    @Override
    public RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b) {
        BiFunction<Number, Number, Number> operation = toFunction(priority);

        return new MathOperationAction(returnType, a, b) {
            @Override
            public Number get(ProcessStack stack, Object instance, Number a, Number b) {
               return operation.apply(a, b);
            }
        };
    }

    private BiFunction<Number, Number, Number> toFunction(int priority) {
        switch (priority) {
            case BYTE:
                return (a, b) -> a.byteValue() + b.byteValue();
            case SHORT:
                return (a, b) -> a.shortValue() + b.shortValue();
            case INT:
                return (a, b) -> a.intValue() + b.intValue();
            case LONG:
                return (a, b) -> a.longValue() + b.longValue();
            case FLOAT:
                return (a, b) -> a.floatValue() + b.floatValue();
            case DOUBLE:
                return (a, b) -> a.doubleValue() + b.doubleValue();
            default:
                throw new PandaParserException("Unknown type " + priority);
        }
    }

}
