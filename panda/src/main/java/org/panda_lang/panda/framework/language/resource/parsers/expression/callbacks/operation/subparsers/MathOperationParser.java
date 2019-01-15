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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers;

import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.Operation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationParser;

import java.util.Stack;

public class MathOperationParser implements OperationParser {

    @Override
    public ExpressionCallback parse(ParserData data, Operation operation) {
        Stack<Object> math = new Stack<>();
        Stack<Token> operators = new Stack<>();

        for (Operation.OperationElement element : operation.getElements()) {
            if (element.isExpression()) {
                math.push(element.getExpression());
                continue;
            }

            Token operator = element.getOperator().getToken();

            switch (operator.getTokenValue()) {
                case "+":
                case "-":
                case "*":
                case "/":
                    if (operators.size() != 0) {
                        if (compare(operators.peek(), operator)) {
                            math.push(operators.pop());
                        }
                    }

                    operators.push(operator);
                    break;
                case "(":
                    operators.push(operator);
                    break;
                case ")":
                    while (!operators.peek().getTokenValue().equals("(")) {
                        math.push(operators.pop());
                    }

                    operators.pop();
                    break;
                default:
                    throw new PandaParserException("Unexpected or unsupported operator " + operator);
            }
        }

        while (operators.size() != 0) {
            math.push(operators.pop());
        }

        ModulePath registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        return new MathExpressionCallback(registry, math);
    }

    public boolean compare(Token prev, Token current) {
        return getOrder(prev.getTokenValue()) >= getOrder(current.getTokenValue());
    }

    public int getOrder(String tokenValue) {
        switch (tokenValue) {
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

}
