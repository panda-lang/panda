package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.Panda;

import java.util.ArrayList;
import java.util.Collection;

public class ParserLayout {

    private final Parser parser;
    private Collection<ParserPattern> patterns;

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

    public ParserLayout pattern(ParserPattern pattern) {
        patterns.add(pattern);
        if (pattern.getParser() == null) {
            pattern.parser(parser);
        }
        return this;
    }

    public ParserPattern pattern(String pattern, double priority) {
        ParserPattern p = new ParserPattern(parser, pattern, priority);
        patterns.add(p);
        return p;
    }

    public ParserPattern pattern(String pattern, double priority, int id) {
        ParserPattern p = pattern(pattern, priority);
        p.setID(id);
        return p;
    }

    public ParserPattern pattern(String pattern, double priority, int id, char[] charset) {
        ParserPattern p = pattern(pattern, priority, id);
        p.setCharset(charset);
        return p;
    }

    public void register(Panda panda) {
        panda.getPandaCore().registerParser(this);
    }

    public Collection<ParserPattern> getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
