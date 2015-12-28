package org.panda_lang.panda.core.parser;

import java.util.ArrayList;
import java.util.Collection;

public class ParserLayout {

    private final Parser parser;
    private Collection<Pattern> patterns;

    public ParserLayout(Parser parser, String pattern) {
        this(parser, pattern, 0);
    }

    public ParserLayout(Parser parser, String pattern, double priority) {
        this(parser);
        this.pattern(pattern, priority);
    }

    public ParserLayout(Parser parser) {
        this.parser = parser;
        this.patterns = new ArrayList<>();
    }

    public ParserLayout pattern(Pattern pattern) {
        patterns.add(pattern);
        if (pattern.getParser() == null) {
            pattern.parser(parser);
        }
        return this;
    }

    public Pattern pattern(String pattern, double priority) {
        Pattern p = new Pattern(parser, pattern, priority);
        patterns.add(p);
        return p;
    }

    public Pattern pattern(String pattern, double priority, int id) {
        Pattern p = pattern(pattern, priority);
        p.setID(id);
        return p;
    }

    public Pattern pattern(String pattern, double priority, int id, char[] charset) {
        Pattern p = pattern(pattern, priority, id);
        p.setCharset(charset);
        return p;
    }

    public Collection<Pattern> getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
