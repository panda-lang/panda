package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.util.MathBuilder;
import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.statement.Math;

import java.util.Stack;
import java.util.StringTokenizer;

public class MathParser implements Parser {

    @Override
    public Math parse(Atom atom) {
        MathBuilder mathBuilder = new MathBuilder();
        Stack<Character> operators = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(atom.getSourcesDivider().getLine(), "+-*/^()", true);

        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (token.isEmpty()) {
                continue;
            }

            char c = token.charAt(0);
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    if (operators.size() != 0) {
                        if (compare(operators.peek(), c)) {
                            mathBuilder.append(operators.pop());
                        }
                    }
                    operators.push(c);
                    break;
                case '(':
                    operators.push(c);
                    break;
                case ')':
                    while (operators.peek() != '(') {
                        mathBuilder.append(operators.pop());
                    }
                    operators.pop();
                    break;
                default:
                    atom.setSourceCode(token);
                    Factor factor = new FactorParser().parse(atom);
                    mathBuilder.append(factor);
                    break;
            }

        }

        while (operators.size() != 0) {
            mathBuilder.append(operators.pop());
        }

        mathBuilder.rewrite();
        return new Math(mathBuilder);
    }

    public boolean compare(char prev, char current) {
        return getOrder(prev) >= getOrder(current);
    }

    public int getOrder(char c) {
        int i = c == '*' || c == '/' || c == '^' ? 2 : 0;
        return i == 0 && (c == '+' || c == '-') ? 1 : i;
    }


}
