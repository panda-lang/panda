package org.panda_lang.panda.core.parser.essential.assistant;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Pattern;
import org.panda_lang.panda.core.parser.PatternExtractor;
import org.panda_lang.panda.core.syntax.Operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class FactorAssistant {

    public static boolean isMethod(Atom atom, String parameter) {
        String pattern = atom.getPatternExtractor().extract(parameter, new char[]{'(', ')'});
        return pattern.equals("()");
    }

    public static boolean isMath(String parameter) {
        boolean string = false;
        for (char c : parameter.toCharArray()) {
            if (c == '"') {
                string = !string;
                continue;
            } else if (string) {
                continue;
            }
            if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                return true;
            }
        }
        return false;
    }

    public static boolean isEquality(Atom atom, String parameter) {
        String extract = atom.getPatternExtractor().extract(parameter, PatternExtractor.DEFAULT);
        for (Operator operator : Operator.getOperators(1)) {
            Pattern pattern = new Pattern(operator.getOperator());
            if (pattern.match(extract)) {
                return true;
            }
        }
        return false;
    }

    public static String[] split(String source) {
        Collection<String> parameters = new ArrayList<>();
        StringBuilder node = new StringBuilder();
        Stack<Character> characters = new Stack<>();
        char[] chars = source.toCharArray();
        boolean string = false;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '"') {
                string = !string;
                node.append(c);
                continue;
            } else if (string) {
                node.append(c);
                continue;
            } else if (node.length() == 0 && Character.isWhitespace(c)) {
                continue;
            }

            switch (c) {
                case '(':
                    characters.push(c);
                    node.append(c);
                    break;
                case ')':
                    if (characters.size() != 0) {
                        characters.pop();
                    }
                    node.append(c);
                    break;
                case ',':
                    if (characters.size() == 0) {
                        String parameter = node.toString();
                        node.setLength(0);
                        parameters.add(parameter);
                        break;
                    }
                default:
                    node.append(c);
                    break;
            }
        }
        if (node.length() != 0) {
            String parameter = node.toString();
            node.setLength(0);
            parameters.add(parameter);
        }

        return parameters.toArray(new String[parameters.size()]);
    }

}
