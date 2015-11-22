package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.parser.improved.Parser;

public class ParserScheme {

    private final Parser parser;
    private final String[] patterns;

    public ParserScheme(Parser parser, String... patterns) {
        this.parser = parser;
        this.patterns = patterns;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
