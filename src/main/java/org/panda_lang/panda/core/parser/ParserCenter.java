package org.panda_lang.panda.core.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ParserCenter {

    private static final List<Pattern> patterns = new ArrayList<>();

    public static void registerParser(ParserLayout parserLayout) {
        registerPatterns(parserLayout.getPatterns());
    }

    public static void registerPatterns(Collection<Pattern> patternCollection) {
        patterns.addAll(patternCollection);
        Collections.sort(patterns);
    }

    public static List<Pattern> getPatterns() {
        return patterns;
    }

    public static Parser getParser(String s) {
        for (Pattern pattern : getPatterns()) {
            if (pattern.match(s)) {
                return pattern.getParser();
            }
        }
        return null;
    }

}
