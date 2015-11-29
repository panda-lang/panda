package org.panda_lang.panda.core.parser.improved;

import java.util.Stack;

public class PatternExtractor {

    /* Patterns {DEFAULT}

        *{          | class Test {

        *=*;        | int classTVar = 1;

        //*         | // xyz

        *{          | method func() {

        *;          | System.print(1);

        *{          | } else {

        *;          | System.print("Hello Panda :>");

        *=*;        | list = new List();

        *{          | thread(testThread) {

     */

    public static final char[] DEFAULT = new char[]{'{', '}', '=', '/', ';', '#', ':', '?', '(', ')'};

    public String extract(String line, char[] set) {
        if (line == null) return "";

        StringBuilder pattern = new StringBuilder();
        Stack<Character> sections = new Stack<>();
        boolean string = false;

        for (char c : line.toCharArray()) {

            // {string}
            if (c == '"') {
                string = !string;
                continue;
            } else if (string) {
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
