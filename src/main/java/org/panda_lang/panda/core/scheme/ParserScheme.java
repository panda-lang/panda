package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.parser.improved.Parser;

public class ParserScheme {

    private final String[] patterns;
    private final Parser<?> parser;

    public ParserScheme(String[] patterns, Parser<?> parser) {
        this.patterns = patterns;
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }

    public String[] getPatterns() {
        return patterns;
    }

}
