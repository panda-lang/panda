package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import java.util.ArrayList;
import java.util.List;

public class ExtractorResult {

    private final boolean matched;
    private final String message;
    private final List<String> identifiers = new ArrayList<>();

    public ExtractorResult(boolean matched) {
        this.matched = matched;
        this.message = "<unknown>";
    }

    public ExtractorResult(String message) {
        this.matched = false;
        this.message = message;
    }

    public ExtractorResult identified(String identifier) {
        identifiers.add(identifier);
        return this;
    }

    public boolean hasErrorMessage() {
        return message != null;
    }

    public boolean isMatched() {
        return matched;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

}
