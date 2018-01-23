package org.panda_lang.panda.language.structure.general.expression.callbacks.math;

import org.panda_lang.panda.core.interpreter.lexer.extractor.vague.VagueElement;
import org.panda_lang.panda.core.interpreter.lexer.extractor.vague.VagueExtractor;
import org.panda_lang.panda.core.interpreter.lexer.extractor.vague.VagueResult;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.Separator;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.language.structure.general.expression.Expression;
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

        return new MathExpressionCallback(math);
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
