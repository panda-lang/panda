/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.design.interpreter.pattern.lexical.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.MatcherResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.processed.ProcessedValue;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LexicalExtractorResult<T> implements MatcherResult {

    private static final LexicalExtractorResult<?> ERR = new LexicalExtractorResult<>(false);

    private Snippet source;
    private final boolean matched;
    private final List<String> wildcards;
    private final List<String> identifiers;
    private final List<ProcessedValue<T>> processedValues;

    public LexicalExtractorResult() {
        this(true);
    }

    private LexicalExtractorResult(boolean matched) {
        this.matched = matched;
        this.identifiers = matched ? new ArrayList<>() : null;
        this.wildcards = matched ? new ArrayList<>() : null;
        this.processedValues = matched ? new ArrayList<>() : null;
    }

    public void merge(LexicalExtractorResult<T> result) {
        if (!result.isMatched()) {
            throw new RuntimeException("Cannot merge unmatched result");
        }

        processedValues.addAll(result.processedValues);
        identifiers.addAll(result.identifiers);
        wildcards.addAll(result.wildcards);
    }

    public LexicalExtractorResult<T> addWildcard(@Nullable String wildcardContent) {
        if (!StringUtils.isEmpty(wildcardContent)) {
            wildcards.add(wildcardContent);
        }

        return this;
    }

    public LexicalExtractorResult<T> addIdentifier(@Nullable String identifier) {
        if (!StringUtils.isEmpty(identifier)) {
            identifiers.add(identifier);
        }

        return this;
    }

    public LexicalExtractorResult<T> addProcessedValue(@Nullable ProcessedValue<T> processedValue) {
        if (processedValue != null) {
            processedValues.add(processedValue);
        }

        return this;
    }

    protected LexicalExtractorResult<T> withSource(Snippetable source) {
        this.source = source.toSnippet();
        return this;
    }

    @Override
    public boolean isMatched() {
        return matched;
    }

    public List<ProcessedValue<T>> getProcessedValues() {
        return processedValues;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public List<String> getWildcards() {
        return wildcards;
    }

    @Override
    public Snippet getSource() {
        return source;
    }

    @SuppressWarnings("unchecked")
    public static <T> LexicalExtractorResult<T> err() {
        return (LexicalExtractorResult<T>) ERR;
    }

}
