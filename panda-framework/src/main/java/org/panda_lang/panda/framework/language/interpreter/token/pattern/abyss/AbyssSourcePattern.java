package org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss;

public class AbyssSourcePattern {

    private final String source;

    private AbyssSourcePattern(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public static AbyssSourcePattern of(String pattern) {
        return new AbyssSourcePattern(pattern);
    }

}
