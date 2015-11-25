package org.panda_lang.panda.core.parser.depracted.util;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.depracted.ParameterParser;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;

import java.util.Stack;

public class MethodParserUtils {

    public static MethodInfo getMethodIndication(Block block, String string) {
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
            } else if (s) {
                node.append(c);
                continue;
            } else if (p) {
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    stack.pop();
                    if (stack.size() == 0) {
                        break;
                    }
                }
                node.append(c);
                continue;
            } else if (Character.isWhitespace(c)) {
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
        ParameterParser parser = new ParameterParser(params);
        Parameter[] parameters = parser.parse(block);

        Parameter instance;
        if (object != null) {
            boolean io = false;
            for (ObjectScheme os : ElementsBucket.getObjects()) {
                if (object.equals(os.getName())) {
                    io = true;
                    return new MethodInfo(object, method, parameters);
                }
            }
            if (!io) {
                instance = new ParameterParser().parse(block, object);
                return new MethodInfo(instance, method, parameters);
            }
        }
        return new MethodInfo(method, parameters);
    }


}
