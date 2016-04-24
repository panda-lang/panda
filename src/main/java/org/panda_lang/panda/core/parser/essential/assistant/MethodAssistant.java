package org.panda_lang.panda.core.parser.essential.assistant;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.MethodInfo;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Vial;

import java.util.Stack;

public class MethodAssistant {

    public static MethodInfo getMethodIndication(ParserInfo parserInfo, String string) {
        StringBuilder node = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        char[] chars = string.toCharArray();
        boolean s = false,
                p = false;
        String object = null,
                method = null;

        for (int i = 0; i < string.length(); i++) {
            char c = chars[i];

            if (c == '"') {
                s = !s;
            }
            else if (s) {
                node.append(c);
                continue;
            }
            else if (p) {
                if (c == '(') {
                    stack.push(c);
                }
                else if (c == ')') {
                    stack.pop();
                    if (stack.size() == 0) {
                        break;
                    }
                }
                node.append(c);
                continue;
            }
            else if (Character.isWhitespace(c)) {
                continue;
            }

            switch (c) {
                case '(':
                    method = node.toString();
                    node.setLength(0);
                    stack.push(c);
                    p = true;
                    break;
                case '.':
                    object = node.toString();
                    node.setLength(0);
                    break;
                case ';':
                    break;
                default:
                    node.append(c);
                    break;
            }
        }

        String params = node.toString();
        node.setLength(0);
        parserInfo.setSourceCode(params);
        FactorParser parser = new FactorParser();
        RuntimeValue[] runtimeValues = parser.splitAndParse(parserInfo);

        RuntimeValue instance;
        if (object != null) {
            Vial vial = parserInfo.getDependencies().getVial(object);
            if (vial != null) {
                return new MethodInfo(vial, method, runtimeValues);
            }
            instance = new FactorParser().parse(parserInfo, object);
            return new MethodInfo(instance, method, runtimeValues);
        }
        return new MethodInfo(method, runtimeValues);
    }


}
