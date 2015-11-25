package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Math;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.util.MathBuilder;

import java.util.Stack;
import java.util.StringTokenizer;

public class MathParser {

    private final String source;

    public MathParser(String source) {
        this.source = source;
    }

    public Parameter parse(Block block) {
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
                    Atom atom = new Atom();
                    atom.setParent(block);
                    atom.setSourceCode(token);
                    Parameter parameter = new ParameterParser().parse(atom);
                    mathBuilder.append(parameter);
                    break;
            }

        }

        while (operators.size() != 0) {
            mathBuilder.append(operators.pop());
        }

        mathBuilder.rewrite();
        Math math = new Math(mathBuilder);
        return new Parameter("Number", block, new Runtime(math));
    }

    public boolean compare(char prev, char current) {
        return getOrder(prev) >= getOrder(current);
    }

    public int getOrder(char c) {
        int i = c == '*' || c == '/' || c == '^' ? 2 : 0;
        return i == 0 && (c == '+' || c == '-') ? 1 : i;
    }


}
