package org.panda_lang.panda.core.parser.essential.assistant;

public class VariableAssistant {

    public static String[] splitAndClear(String source) {
        String[] ss = source.split("=", 2);
        if (ss.length != 2) {
            return null;
        }
        ss[0] = clear(ss[0]);
        ss[1] = clear(ss[1]);
        return ss;
    }

    public static String clear(String s) {
        StringBuilder node = new StringBuilder();
        boolean string = false;
        for (char c : s.toCharArray()) {
            if (!string && Character.isWhitespace(c)) {
                if (node.toString().equals("new")) {
                    node.append(c);
                }
            } else if (string || c != ';') {
                if (c == '"') {
                    string = !string;
                }
                node.append(c);
            }
        }
        return node.toString();
    }

}
