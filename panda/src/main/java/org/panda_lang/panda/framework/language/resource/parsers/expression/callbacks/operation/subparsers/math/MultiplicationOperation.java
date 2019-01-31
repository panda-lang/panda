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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.math;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.rpn.RPNOperationAction;

public class MultiplicationOperation extends MathOperation {


    @Override
    public RPNOperationAction of(Expression a, Expression b) {
        ClassPrototype returnType = super.returnType(a.getReturnType(), b.getReturnType());
        int priority = super.getPriority(returnType);

        return new MathOperationAction(returnType(a.getReturnType(), b.getReturnType())) {
            @Override
            public Object get(ExecutableBranch branch, Value aValue, Value bValue) {
                Number a = aValue.getValue();
                Number b = bValue.getValue();

                switch (priority) {
                    case BYTE:
                        return a.byteValue() * b.byteValue();
                    case SHORT:
                        return a.shortValue() * b.shortValue();
                    case INT:
                        return a.intValue() * b.intValue();
                    case LONG:
                        return a.longValue() * b.longValue();
                    case FLOAT:
                        return a.floatValue() * b.floatValue();
                    case DOUBLE:
                        return a.doubleValue() * b.doubleValue();
                    default:
                        throw new PandaParserException("Unknown type " + priority);
                }
            }
        };
    }


}
