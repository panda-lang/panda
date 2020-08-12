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

package org.panda_lang.language.interpreter.pattern.functional.verifiers;

import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.pattern.functional.Verifier;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.QuadPredicate;

import java.util.List;

public class NextVerifier implements Verifier<Snippetable> {

    private final QuadPredicate<List<Pair<String, Object>>, SynchronizedSource, Snippetable, TokenInfo> predicate;

    public NextVerifier(QuadPredicate<List<Pair<String, Object>>, SynchronizedSource, Snippetable, TokenInfo> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean verify(List<Pair<String, Object>> results, SynchronizedSource source, Snippetable content) {
        if (!source.hasNext()) {
            return false;
        }

        return predicate.test(results, source, content, source.getNext());
    }

}
