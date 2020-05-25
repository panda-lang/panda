/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.custom;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.language.interpreter.pattern.PatternMapping;
import org.panda_lang.framework.language.interpreter.pattern.PatternResult;

import java.util.Map;

public final class Result implements PatternResult, PatternMapping {

    public static final Result NOT_MATCHED = new Result(null, null);

    private final Snippet source;
    protected final Map<String, Object> results;

    public Result(Snippet source, Map<String, Object> results) {
        this.source = source;
        this.results = results;
    }

    public boolean has(String id) {
        return results.containsKey(id);
    }

    public boolean has(Token token) {
        return results.containsKey(token.getValue());
    }

    @Override
    public boolean isMatched() {
        return results != null;
    }

    public <T> T get(String id, @SuppressWarnings("unused") Class<T> type) {
        return get(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String id) {
        return (T) results.get(id);
    }

    @Override
    public Snippet getSource() {
        return source;
    }

}
