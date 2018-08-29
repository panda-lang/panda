/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.callbacks.math;

import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.module.PrimitivePrototypeLiquid;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.util.Stack;

public class MathExpressionCallback implements ExpressionCallback {

    private final ModulePath modulePath;
    private final Stack<Object> mathStack;

    public MathExpressionCallback(ModulePath modulePath, Stack<Object> mathStack) {
        this.mathStack = mathStack;
        this.modulePath = modulePath;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        Stack<Value> values = new Stack<>();

        for (Object mathElement : mathStack) {
            if (mathElement instanceof Token) {
                Token operator = (Token) mathElement;

                Value a = values.pop();
                Value b = values.pop();

                // TODO: Impl add()/subtract()/multiply()/divide() methods for diff prototypes
                int aValue = a.getValue();
                int bValue = b.getValue();
                int cValue;

                // TODO: Dedicated callback for each type of operators
                switch (operator.getTokenValue()) {
                    case "+":
                        cValue = aValue + bValue;
                        break;
                    case "-":
                        cValue = aValue - bValue;
                        break;
                    case "*":
                        cValue = aValue * bValue;
                        break;
                    case "/":
                        cValue = aValue / bValue;
                        break;
                    default:
                        throw new PandaRuntimeException("Unknown operator");
                }

                Value c = new PandaValue(PrimitivePrototypeLiquid.INT, cValue);
                values.push(c);
            }
            else {
                Expression mathExpression = (Expression) mathElement;
                values.push(mathExpression.getExpressionValue(branch));
            }
        }

        return values.pop();
    }

    public ClassPrototype getReturnType() {
        return PrimitivePrototypeLiquid.INT;
    }

}
