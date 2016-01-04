package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Math;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.util.MathBuilder;

import java.util.Stack;
import java.util.StringTokenizer;

public class MathParser implements Parser {

    private final String source;

    public MathParser(String source) {
        this.source = source;
    }

    @Override
    public Factor parse(Atom atom) {
        MathBuilder mathBuilder = new MathBuilder();
        Stack<Character> operators = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(source, "+-*/^()", true);

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
        Math math = new Math(mathBuilder);
        return new Factor("Number", atom.getParent().getVariables(), new Runtime(math));
    }

    public boolean compare(char prev, char current) {
        return getOrder(prev) >= getOrder(current);
    }

    public int getOrder(char c) {
        int i = c == '*' || c == '/' || c == '^' ? 2 : 0;
        return i == 0 && (c == '+' || c == '-') ? 1 : i;
    }


}
