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

package org.panda_lang.panda.language.structure.general.expression.callbacks.math;

import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.defaults.Separator;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.vague.VagueElement;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.vague.VagueExtractor;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.vague.VagueResult;
import org.panda_lang.panda.framework.language.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.syntax.tokens.Operators;
import org.panda_lang.panda.language.syntax.tokens.Separators;

import java.util.Stack;

public class MathParser implements Parser {

    protected static final VagueExtractor EXTRACTOR = new VagueExtractor(new Separator[]{
            Separators.LEFT_PARENTHESIS_DELIMITER,
            Separators.RIGHT_PARENTHESIS_DELIMITER
    }, new Token[] {
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION });

    public MathExpressionCallback parse(TokenizedSource source, ParserInfo info) {
        Environment environment = info.getComponent(Components.ENVIRONMENT);
        ModuleRegistry registry = environment.getModuleRegistry();

        TokenReader reader = new PandaTokenReader(source);
        VagueResult result = EXTRACTOR.extract(reader);

        Stack<Object> math = new Stack<>();
        Stack<Token> operators = new Stack<>();
        ExpressionParser expressionParser = new ExpressionParser();

        for (VagueElement element : result.getElements()) {
            if (element.isExpression()) {
                Expression expression = expressionParser.parse(info, element.getExpression());
                math.push(expression);
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
                    throw new PandaParserException("Unexpected operator " + operator);
            }
        }

        while (operators.size() != 0) {
            math.push(operators.pop());
        }

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
                // throw new PandaParserException("Unexpected token " + tokenValue);
        }
    }

}
