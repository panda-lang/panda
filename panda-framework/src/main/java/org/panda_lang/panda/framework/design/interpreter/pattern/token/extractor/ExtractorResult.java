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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExtractorResult {

    private final boolean matched;
    private final String errorMessage;
    private final List<String> identifiers = new ArrayList<>();
    private final Map<String, Snippet> wildcards = new LinkedHashMap<>();

    public ExtractorResult() {
        this.matched = true;
        this.errorMessage = null;
    }

    public ExtractorResult(String errorMessage) {
        this.matched = false;
        this.errorMessage = errorMessage;
    }

    public ExtractorResult merge(ExtractorResult otherResult) {
        if (!otherResult.isMatched()) {
            throw new RuntimeException("Cannot merge unmatched result");
        }

        identifiers.addAll(otherResult.identifiers);
        wildcards.putAll(otherResult.wildcards);
        return this;
    }

    public ExtractorResult exclude(ExtractorResult otherResult) {
        if (!otherResult.isMatched()) {
            throw new RuntimeException("Cannot merge unmatched result");
        }

        identifiers.removeAll(otherResult.identifiers);
        wildcards.keySet().removeAll(otherResult.wildcards.keySet());
        return this;
    }

    public ExtractorResult identified(String identifier) {
        identifiers.add(identifier);
        return this;
    }

    public ExtractorResult addWildcard(String name, Snippet wildcardContent) {
        wildcards.put(name, wildcardContent);
        return this;
    }

    protected int contentLength() {
        int count = 0;

        for (Snippet value : wildcards.values()) {
            count += value.size();
        }

        return count;
    }

    public boolean hasIdentifier(String identifier) {
        return identifiers.contains(identifier);
    }

    public boolean hasErrorMessage() {
        return errorMessage != null;
    }

    public boolean isMatched() {
        return matched;
    }

    public Snippet getWildcard(String name) {
        return wildcards.get(name);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, Snippet> getWildcards() {
        return wildcards;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

}
