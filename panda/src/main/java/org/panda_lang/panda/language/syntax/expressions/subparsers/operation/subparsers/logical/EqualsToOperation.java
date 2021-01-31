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

package org.panda_lang.panda.language.syntax.expressions.subparsers.operation.subparsers.logical;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.interpreter.parser.PandaParserException;
import org.panda_lang.framework.runtime.ProcessStack;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.rpn.RPNSimplifiedSupplier;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.subparsers.number.NumericOperation;

import java.util.Objects;
import java.util.function.BiFunction;

import static org.panda_lang.panda.language.syntax.expressions.subparsers.number.NumberPriorities.*;

public class EqualsToOperation extends RPNSimplifiedSupplier<Object, Object, Boolean> {

    @Override
    public RPNOperationAction<Boolean> of(TypeLoader moduleLoader, Expression a, Expression b) {
        Type numberType = moduleLoader.requireType("panda::Number");

        if (numberType.isAssignableFrom(a.getKnownType()) && numberType.isAssignableFrom(b.getKnownType())) {
            int priority = NumericOperation.getHigherPriority(a.getKnownType(), b.getKnownType());
            BiFunction<Number, Number, Boolean> numericEquals = toFunction(priority);

            return new ComparisonOperatorAction(a, b) {
                @Override
                public Boolean get(ProcessStack stack, Object instance, Number a, Number b) {
                    return numericEquals.apply(a, b);
                }
            };
        }

        return super.of(moduleLoader, a, b);
    }

    @Override
    public Boolean get(ProcessStack stack, Object instance, Object a, Object b) {
        return Objects.equals(a, b);
    }

    private BiFunction<Number, Number, Boolean> toFunction(int priority) {
        switch (priority) {
            case BYTE:
                return (a, b) -> a.byteValue() == b.byteValue();
            case SHORT:
                return (a, b) -> a.shortValue() == b.shortValue();
            case INT:
                return (a, b) -> a.intValue() == b.intValue();
            case LONG:
                return (a, b) -> a.longValue() == b.longValue();
            case FLOAT:
                return (a, b) -> a.floatValue() == b.floatValue();
            case DOUBLE:
                return (a, b) -> a.doubleValue() == b.doubleValue();
            default:
                throw new PandaParserException("Unknown type " + priority);
        }
    }

    @Override
    public Type returnType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda::Bool");
    }

    @Override
    public Type requiredType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda::Object");
    }

}
