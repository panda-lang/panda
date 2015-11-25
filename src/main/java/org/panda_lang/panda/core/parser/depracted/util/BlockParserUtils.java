package org.panda_lang.panda.core.parser.depracted.util;

import java.util.ArrayList;
import java.util.List;

public class BlockParserUtils {

    public static BlockInfo getSectionIndication(String s) {
        String name = null;
        List<String> specifiers = new ArrayList<>();
        List<String> parameters = new ArrayList<>();

        // {build.tools}
        StringBuilder node = new StringBuilder();
        boolean string = false;
        boolean spec = false;
        boolean pam = false;

        // {parse}
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
                    if (!string) spec = true;
                case ')':
                case ',':
                case '{':
                    if (!string) c = ' ';
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

}
