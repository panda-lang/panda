package org.panda_lang.panda.core.parser.improved.util;

import java.util.Stack;

public class PatternExtractor {

    /*

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

    public static final char[] DEFAULT = new char[] { '{', '=', '/', '*', ';' };

    public String extract(String line, char[] set) {
        StringBuilder pattern = new StringBuilder();
        Stack<Character> sections = new Stack<>();
        boolean string = false;

        for(char c : line.toCharArray()) {

            // {string}
            if(c == '"') {
                string = !string;
                continue;
            } else if(string) {
                continue;
            }

            switch (c) {
                case '(':
                    sections.push(c);
                    break;
                case ')':
                    sections.pop();
                    break;
                default:
                    if(sections.size() > 0) {
                        continue;
                    }
                    break;
            }

            for(char s : set) {
                if(s == c) {
                    pattern.append(c);
                    break;
                }
            }
        }
        return pattern.toString();
    }

}
