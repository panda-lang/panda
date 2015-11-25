package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.Pattern;

public class ParserScheme {

    private final Parser parser;
    private final Pattern pattern;

    public ParserScheme(Parser parser, String... patterns) {
        this.parser = parser;
        this.pattern = new Pattern(patterns);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Parser getParser() {
        return parser;
    }

}
