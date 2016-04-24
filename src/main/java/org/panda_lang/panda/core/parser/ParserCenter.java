package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.parser.util.match.parser.ParserPattern;
import org.panda_lang.panda.core.parser.util.match.parser.PatternExtractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ParserCenter {

    private final List<ParserPattern> patterns = new ArrayList<>();

    public void registerParser(ParserLayout parserLayout) {
        registerPatterns(parserLayout.getPatterns());
    }

    public void registerPatterns(Collection<ParserPattern> patternCollection) {
        patterns.addAll(patternCollection);
        Collections.sort(patterns);
    }

    public Parser getParser(ParserInfo parserInfo, String s) {
        final String defaultPattern = parserInfo.getPatternExtractor().extract(s, PatternExtractor.DEFAULT);
        for (ParserPattern pattern : getPatterns()) {
            String matchedPattern = pattern.getCharset() != null ? parserInfo.getPatternExtractor().extract(s, pattern.getCharset()) : defaultPattern;
            if (pattern.match(matchedPattern)) {
                parserInfo.setVariant(pattern);
                return pattern.getParser();
            }
        }
        return null;
    }

    public List<ParserPattern> getPatterns() {
        return patterns;
    }

}
