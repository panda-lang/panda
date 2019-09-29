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

package org.panda_lang.framework.language.interpreter.pattern.linear;

import org.panda_lang.framework.design.interpreter.pattern.PatternResult;
import org.panda_lang.framework.design.interpreter.pattern.PatternMapping;
import org.panda_lang.framework.design.interpreter.token.Snippet;

import java.util.List;
import java.util.Map;

public class LinearPatternResult implements PatternResult, PatternMapping {

    protected static final LinearPatternResult NOT_MATCHED = new LinearPatternResult();

    private final Snippet source;
    private final List<String> identifiers;
    private final Map<String, Object> wildcards;

    LinearPatternResult(Snippet source, List<String> identifiers, Map<String, Object> wildcards) {
        this.source = source;
        this.identifiers = identifiers;
        this.wildcards = wildcards;
    }

    private LinearPatternResult() {
        this(null, null, null);
    }

    @Override
    public boolean isMatched() {
        return identifiers != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String identifier) {
        return (T) wildcards.get(identifier);
    }

    public List<? extends String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public Snippet getSource() {
        return source;
    }

}
