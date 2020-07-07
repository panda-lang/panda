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

package org.panda_lang.framework.language.interpreter.pattern.functional;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.pattern.Mappings;
import org.panda_lang.framework.language.interpreter.pattern.PatternResult;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;

public final class FunctionalResult implements PatternResult, Mappings {

    public static final FunctionalResult NOT_MATCHED = new FunctionalResult(null, null);

    private final Snippet source;
    protected final List<Pair<String, Object>> results;

    public FunctionalResult(Snippet source, List<Pair<String, Object>> results) {
        this.source = source;
        this.results = results;
    }

    @Override
    public boolean isMatched() {
        return results != null;
    }

    @Override
    public <T> Option<T> get(String id) {
        for (Pair<String, Object> result : results) {
            if (result.getKey().equals(id)) {
                return Option.of(ObjectUtils.cast(result.getValue()));
            }
        }

        return Option.none();
    }

    @Override
    public Snippet getSource() {
        return source;
    }

}
