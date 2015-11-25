package org.panda_lang.panda.core.parser.improved;

public class Pattern {

    private final String pattern;

    public Pattern(String pattern) {
        this.pattern = pattern.replace(" ", "");
    }

    public boolean match(String s) {
        char[] pattern = this.pattern.toCharArray();
        char character = '*';
        int i = 0;
        for (char c : s.toCharArray()) {
            char cc = pattern.length < i ? pattern[i] : character;
            if (cc == c || cc == '*' || character == '*') {
                character = cc;
                i++;
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
