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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberPriorities;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;

public final class GreaterThanOrEqualsOperator extends ComparisonOperator {

    @Override
    @SuppressWarnings("DuplicatedCode")
    public RPNOperationAction<Boolean> of(int compared, Expression a, Expression b) {
        return new ComparisonOperatorAction(a, b) {
            @Override
            public Boolean get(ProcessStack stack, Object instance, Number a, Number b) {
                switch (compared) {
                    case NumberPriorities.INT:
                        return a.intValue() >= b.intValue();
                    case NumberPriorities.LONG:
                        return a.longValue() >= b.longValue();
                    case NumberPriorities.DOUBLE:
                        return a.doubleValue() >= b.doubleValue();
                    case NumberPriorities.FLOAT:
                        return a.floatValue() >= b.floatValue();
                    case NumberPriorities.BYTE:
                        return a.byteValue() >= b.byteValue();
                    case NumberPriorities.SHORT:
                        return a.shortValue() >= b.shortValue();
                    default:
                        throw new PandaParserException("Unknown type " + compared);
                }
            }
        };
    }

}
