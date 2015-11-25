package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.Pattern;

public class ParserScheme {

    private final Parser parser;
    private final Pattern[] patterns;

    public ParserScheme(Parser parser, String... patterns) {
        this.parser = parser;
        this.patterns = new Pattern[patterns.length];

        for (int i = 0; i < patterns.length; i++) {
            this.patterns[i] = new Pattern(patterns[i]);
        }
    }

    public Pattern[] getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
