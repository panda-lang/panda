package org.panda_lang.panda.core.parser.improved.essential.assistant;

import org.panda_lang.panda.core.parser.improved.essential.util.BlockInfo;

import java.util.ArrayList;
import java.util.List;

public class VialAssistant {

    public static String extractIndication(String line) {
        StringBuilder node = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (Character.isWhitespace(c) || c == '(' || c == '{') {
                if (node.length() == 0) {
                    continue;
                } else {
                    break;
                }
            } else {
                node.append(c);
            }
        }
        return node.toString();
    }

    public static BlockInfo extractVial(String s) {
        String name = null;
        List<String> specifiers = new ArrayList<>();
        List<String> parameters = new ArrayList<>();

        // {build.tools}
        StringBuilder node = new StringBuilder();
        boolean string = false;
        boolean spec = false;
        boolean pam = false;

        // {parseLocal}
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // {switch.special}
            switch (c) {
                case '"':
                    // {string.skip}
                    string = !string;
                    break;
                case '(':
                    // {parameters.start}
                    if (!string) {
                        spec = true;
                    }
                case ')':
                case ',':
                case '{':
                    if (!string) {
                        c = ' ';
                    }
            }

            // {part.end}
            if (Character.isWhitespace(c)) {

                // {empty.continue}
                if (node.length() == 0) continue;

                // {part.append}
                String part = node.toString();

                // {block.type}
                if (name == null) {
                    name = part;
                    if (spec) {
                        pam = true;
                    }
                }
                // {parameters}
                else if (pam) {
                    parameters.add(part);
                }
                // {specifiers}
                else {
                    if (spec) pam = true;
                    specifiers.add(part);
                }

                // {clear}
                node.setLength(0);
                continue;
            }

            // {default.append}
            node.append(c);
        }

        // {build.block.info}
        return new BlockInfo(name, specifiers, parameters);
    }

    public static boolean isPlug(String s) {
        return s.equals("}") || s.equals("};");
    }

}
