package org.panda_lang.panda.core.parser.essential.assistant;

import org.panda_lang.panda.core.parser.essential.util.BlockInfo;

import java.util.ArrayList;
import java.util.List;

public class BlockAssistant {

    public static String extractIndication(String line) {
        StringBuilder node = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (Character.isWhitespace(c) || c == '(' || c == '{') {
                if (node.length() != 0) {
                    break;
                }
            }
            else {
                node.append(c);
            }
        }
        return node.toString();
    }

    public static BlockInfo extractBlock(String s) {
        String name = null;
        List<String> specifiers = new ArrayList<>();
        List<String> parameters = new ArrayList<>();

        // {build.tools}
        StringBuilder node = new StringBuilder();
        boolean string = false;
        boolean spec = false;
        boolean param = false;

        // {splitAndParse}
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            char c = aChar;

            // {switch.special}
            switch (c) {
                case '"':
                    // {string.skip}
                    string = !string;
                    continue;
                case '(':
                    // {runtimeValues.start}
                    if (!string) {
                        param = true;
                        if (node.length() == 0) {
                            spec = false;
                        }
                        else if (name == null) {
                            spec = true;
                        }
                        continue;
                    }
                    break;
                case ')':
                case ',':
                case '{':
                    if (!string) {
                        c = ';';
                    }
                    break;
                case ' ':
                    if (!string && !param) {
                        c = ';';
                    }
            }

            // {part.end}
            if (c == ';') {

                // {empty.continue}
                if (node.length() == 0) {
                    continue;
                }

                // {part.append}
                String part = node.toString();

                // {block.type}
                if (name == null) {
                    name = part;
                    spec = !spec;
                }
                else if (spec && param) {
                    spec = false;
                    specifiers.add(part);
                }
                else if (spec) {
                    specifiers.add(part);
                }
                else if (param) {
                    parameters.add(part);
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
        s = s != null ? s.trim() : "";
        return s.equals("}") || s.equals("};");
    }

}
