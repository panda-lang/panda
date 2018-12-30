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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.popsuted;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class TokenExtractorResult {

    private final boolean matched;
    private final List<String> identifiers;
    private final Map<String, Tokens> wildcards;
    private String errorMessage;

    public TokenExtractorResult(boolean matched) {
        this.matched = matched;
        this.identifiers = matched ? new ArrayList<>() : null;
        this.wildcards = matched ? new LinkedHashMap<>() : null;
        this.errorMessage = matched ? null : "<unknown>";
    }

    public TokenExtractorResult() {
        this(false);
    }

    public TokenExtractorResult(String message) {
        this(false);
        this.errorMessage = message;
    }

    public TokenExtractorResult(String message, String variables, Object... data) {
        this(false);

        StringBuilder content = new StringBuilder(message);
        String[] variableNames = variables.split("\\|");

        for (int i = 0; i < variableNames.length; i++) {
            content.append(" [").append(variableNames[i]).append(": ").append(data[i]).append("]");
        }

        this.errorMessage = content.toString();
    }

    public TokenExtractorResult merge(TokenExtractorResult otherResult) {
        if (!otherResult.isMatched()) {
            throw new RuntimeException("Cannot merge unmatched result");
        }

        identifiers.addAll(otherResult.identifiers);
        wildcards.putAll(otherResult.wildcards);
        return this;
    }

    public TokenExtractorResult addWildcard(String details, Tokens wildcard) {
        if (wildcard != null) {
            wildcards.put(details, wildcard);
        }

        return this;
    }

    public TokenExtractorResult identified(String identifier) {
        if (identifier != null) {
            this.identifiers.add(identifier);
        }

        return this;
    }

    public TokenExtractorResult setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
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
