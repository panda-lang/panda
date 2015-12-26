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
        return this;
    }

    public ParserLayout pattern(String pattern, double priority) {
        patterns.add(new Pattern(parser, pattern, priority));
        return this;
    }

    public Collection<Pattern> getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
