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

package org.panda_lang.panda.framework.design.interpreter.pattern.linear;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LinearPatternResult {

    protected static final LinearPatternResult NOT_MATCHED = new LinearPatternResult();

    private final List<String> identifiers;
    private final Map<String, Object> wildcards;

    LinearPatternResult(List<String> identifiers, Map<String, Object> wildcards) {
        this.identifiers = identifiers;
        this.wildcards = wildcards;
    }

    private LinearPatternResult() {
        this(null, null);
    }

    public boolean isMatched() {
        return identifiers != null;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getWildcard(String identifier) {
        return isMatched() ? Optional.ofNullable((T) wildcards.get(identifier)) : Optional.empty();
    }

    public Map<? extends String, ? extends Object> getWildcards() {
        return wildcards;
    }

    public List<? extends String> getIdentifiers() {
        return identifiers;
    }

}
