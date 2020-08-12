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

public final class AdditionOperation extends MathOperation {

    @Override
    public RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b) {
        return new MathOperationAction(returnType, a, b) {
            @Override
            public Number get(ProcessStack stack, Object instance, Number a, Number b) {
                switch (priority) {
                    case INT:
                        return a.intValue() + b.intValue();
                    case DOUBLE:
                        return a.doubleValue() + b.doubleValue();
                    case LONG:
                        return a.longValue() + b.longValue();
                    case FLOAT:
                        return a.floatValue() + b.floatValue();
                    case BYTE:
                        return a.byteValue() + b.byteValue();
                    case SHORT:
                        return a.shortValue() + b.shortValue();
                    default:
                        throw new PandaParserException("Unknown type " + priority);
                }
            }
        };
    }

}
