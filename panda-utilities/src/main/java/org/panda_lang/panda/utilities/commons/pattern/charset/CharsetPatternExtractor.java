/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.utilities.commons.pattern.charset;

import java.util.Stack;

public class CharsetPatternExtractor {

    public static final char[] DEFAULT = "*^!?/[]{}()<>&#@;:=".toCharArray();
    public static final char[] FULL = "abcdefghijklmnopqrstuvwxyz!?/[]{}()<>#;:=-".toCharArray();
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
