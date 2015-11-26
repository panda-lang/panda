package org.panda_lang.panda.core.parser.improved;

public class Pattern {

    private final String pattern;

    public Pattern(String pattern) {
        this.pattern = pattern.replace(" ", "");
    }

    public boolean match(String s) {
        char[] string = s.toCharArray();
        char[] pattern = this.pattern.toCharArray();
        char current = pattern[0];
        char next = 2 <= pattern.length ? pattern[1] : current;
        int p = 0;
        for (int i = 0; i < string.length; i++) {
            char character = string[i];
            if (character == current || current == '*' && character == next) {
                p++;
                current = p < pattern.length ? pattern[p] : current;
                next = p + 1 < pattern.length ? pattern[p + 1] : current;
            } else if (character == '*') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public String getPattern() {
        return pattern;
    }

}
