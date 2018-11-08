package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import java.util.ArrayList;
import java.util.List;

public class TokenExtractorResult {

    private final boolean matched;
    private final List<String> wildcards;
    private final List<String> identifiers;

    public TokenExtractorResult(boolean matched) {
        this.matched = matched;
        this.identifiers = this.matched ? new ArrayList<>() : null;
        this.wildcards = this.matched ? new ArrayList<>() : null;
    }

    public TokenExtractorResult() {
        this(false);
    }

    public void merge(TokenExtractorResult otherResult) {
        if (!otherResult.isMatched()) {
            throw new RuntimeException("Cannot merge unmatched result");
        }

        identifiers.addAll(otherResult.identifiers);
        wildcards.addAll(otherResult.wildcards);
    }

    public void addIdentifier(String identifier) {
        this.identifiers.add(identifier);
    }

    public boolean isMatched() {
        return matched;
    }

}
