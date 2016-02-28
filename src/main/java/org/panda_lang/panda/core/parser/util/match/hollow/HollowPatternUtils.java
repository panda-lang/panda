package org.panda_lang.panda.core.parser.util.match.hollow;

import java.util.ArrayList;
import java.util.List;

public class HollowPatternUtils {

    public static List<String> toFragments(String lightLine) {
        List<String> fragments = new ArrayList<>();
        StringBuilder fragmentBuilder = new StringBuilder();
        boolean string = false;

        for (char c : lightLine.trim().toCharArray()) {
            if (c == '"') {
                string = !string;
            }
            else if (Character.isWhitespace(c) && !string) {
                if (fragmentBuilder.length() != 0) {
                    String fragment = fragmentBuilder.toString();
                    fragments.add(fragment);
                    fragmentBuilder.setLength(0);
                }
                continue;
            }
            fragmentBuilder.append(c);
        }

        if (fragmentBuilder.length() != 0) {
            String fragment = fragmentBuilder.toString();
            fragments.add(fragment);
        }

        return fragments;
    }

}
