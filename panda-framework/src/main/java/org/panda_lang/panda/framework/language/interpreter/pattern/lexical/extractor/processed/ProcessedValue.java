package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed;

import org.jetbrains.annotations.Nullable;

public class ProcessedValue<T> {

    private final T value;
    private final String identifier;

    public ProcessedValue(T value, @Nullable String identifier) {
        this.value = value;
        this.identifier = identifier;
    }

    public @Nullable String getIdentifier() {
        return identifier;
    }

    public T getValue() {
        return value;
    }

}
