package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

public class ExtractorResult {

    private final boolean matched;
    private final String message;

    public ExtractorResult(boolean matched) {
        this.matched = matched;
        this.message = "<unknown>";
    }

    public ExtractorResult(String message) {
        this.matched = false;
        this.message = message;
    }

    public boolean hasErrorMessage() {
        return message != null;
    }

    public boolean isMatched() {
        return matched;
    }

}
