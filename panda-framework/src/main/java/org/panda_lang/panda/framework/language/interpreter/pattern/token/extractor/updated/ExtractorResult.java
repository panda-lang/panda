package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorResult {

    private final boolean matched;
    private final String errorMessage;
    private final List<String> identifiers = new ArrayList<>();
    private final Map<String, Tokens> wildcards = new HashMap<>();

    public ExtractorResult() {
        this.matched = true;
        this.errorMessage = "<unknown>";
    }

    public ExtractorResult(String errorMessage) {
        this.matched = false;
        this.errorMessage = errorMessage;
    }

    public ExtractorResult identified(String identifier) {
        identifiers.add(identifier);
        return this;
    }

    public ExtractorResult addWildcard(String name, Tokens wildcardContent) {
        return null;
    }

    public boolean hasErrorMessage() {
        return errorMessage != null;
    }

    public boolean isMatched() {
        return matched;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, Tokens> getWildcards() {
        return wildcards;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

}
