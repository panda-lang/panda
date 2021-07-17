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

package panda.interpreter.syntax.expressions.subparsers.operation.subparsers.bitwise;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.number.NumericOperator;

import java.util.function.BiFunction;

public final class RightShiftOperator extends BitwiseOperation {

    @Override
    public RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b) {
        BiFunction<Number, Number, Number> rightShift = toFunction(priority);

        return new NumericOperator(returnType, a, b) {
            @Override
            public Number get(ProcessStack stack, Object instance, Number a, Number b) {
                return rightShift.apply(a, b);
            }
        };
    }

    private BiFunction<Number, Number, Number> toFunction(int priority) {
        switch (priority) {
            case BYTE:
                return (a, b) -> a.byteValue() >> b.byteValue();
            case SHORT:
                return (a, b) -> a.shortValue() >> b.shortValue();
            case INT:
                return (a, b) -> a.intValue() >> b.intValue();
            case LONG:
                return (a, b) -> a.longValue() >> b.longValue();
            case FLOAT:
                throw new PandaParserException("Cannot use right shift operator on float type");
            case DOUBLE:
                throw new PandaParserException("Cannot use right shift operator on double type");
            default:
                throw new PandaParserException("Unknown type " + priority);
        }
    }

}
