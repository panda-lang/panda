package org.panda_lang.panda.core.parser.util.match.parser;

import java.util.Stack;

public class PatternExtractor {


    public static final char[] DEFAULT = "!?/{}()<>#;:=".toCharArray();
    public static final char[] FULL = "abcdefghijklmnopqrstuvwxyz!?/{}()<>#;:=".toCharArray();
    public static final char[] EQUALITY = "|!&<>=".toCharArray();
    public static final char[] METHOD = "()".toCharArray();


    public String extract(String line, char[] set) {
        if (line == null) {
            return "";
        }

        StringBuilder pattern = new StringBuilder();
        Stack<Character> sections = new Stack<>();
        boolean string = false;

        for (char c : line.toCharArray()) {

            // {string}
            if (c == '"') {
                string = !string;
                continue;
            }
            else if (string) {
                continue;
            }

            // {section}
            switch (c) {
                // {open}
                case '(':
                    sections.push(c);
                    break;
                // {close}
                case ')':
                    sections.pop();
                    break;
                // {if.section}
                default:
                    if (sections.size() > 0) {
                        continue;
                    }
                    break;
            }
            // {char.set}
            for (char s : set) {
                if (s == c) {
                    // {append.char.set}
                    pattern.append(c);
                    break;
                }
            }
        }

        // {return.pattern}
        return pattern.toString();
    }

}
