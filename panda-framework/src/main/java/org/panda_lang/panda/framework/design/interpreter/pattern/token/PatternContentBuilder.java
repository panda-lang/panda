package org.panda_lang.panda.framework.design.interpreter.pattern.token;

import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

public class PatternContentBuilder {

    private final StringBuilder contentBuilder = new StringBuilder();

    private PatternContentBuilder() { }

    public PatternContentBuilder element(String identifier, String content) {
        contentBuilder.append(identifier).append(":");
        return element(content);
    }

    public PatternContentBuilder element(String content) {
        contentBuilder.append(content);
        return this;
    }

    public PatternContentBuilder identifier(String identifier) {
        contentBuilder.append(identifier).append(":");
        return this;
    }

    public PatternContentBuilder variant(Object... elements) {
        contentBuilder.append("(").append(ContentJoiner.on("|").join(elements)).append(")");
        return this;
    }

    public PatternContentBuilder optional(String identifier, String element) {
        return identifier(identifier).optional(element);
    }

    public PatternContentBuilder optional(String element) {
        contentBuilder.append("[").append(element).append("]");
        return this;
    }
    
    public String build() {
        return contentBuilder.toString();
    }

    public static PatternContentBuilder create() {
        return new PatternContentBuilder();
    }
    
}
