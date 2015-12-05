package org.pandalang.panda.core.scheme;

import org.pandalang.panda.core.parser.improved.Parser;
import org.pandalang.panda.core.parser.improved.Pattern;

import java.util.ArrayList;
import java.util.Collection;

public class ParserScheme {

    private final Parser parser;
    private Collection<Pattern> patterns;

    public ParserScheme(Parser parser, String pattern) {
        this(parser, pattern, 0);
    }

    public ParserScheme(Parser parser, String pattern, double priority) {
        this(parser);
        this.pattern(pattern, priority);
    }

    public ParserScheme(Parser parser) {
        this.parser = parser;
        this.patterns = new ArrayList<>();
    }

    public ParserScheme pattern(Pattern pattern) {
        patterns.add(pattern);
        return this;
    }

    public ParserScheme pattern(String pattern, double priority) {
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
