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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.collection.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

final class FunctionalPatternMatcher {

    private final FunctionalPattern pattern;
    private final PatternData data;

    FunctionalPatternMatcher(FunctionalPattern pattern, @Nullable PatternData data) {
        this.pattern = pattern;
        this.data = data == null ? PatternData.empty() : data;
    }

    public FunctionalResult match(Snippet source, SourceStream stream) {
        List<Pair<String, Object>> results = new ArrayList<>(pattern.getElements().size() * 2);
        SynchronizedSource synchronizedSource = new SynchronizedSource(source);

        for (Element<?> element : pattern.getElements()) {
            if (!synchronizedSource.hasNext()) {
                if (element.isOptional()) {
                    continue;
                }

                return FunctionalResult.NOT_MATCHED;
            }

            synchronizedSource.cacheIndex();
            Object result = element.getReader().read(data, synchronizedSource);

            // result may be null if element is optional
            if (result == null) {
                // restore index if element is optional
                if (restoreIfOptional(synchronizedSource, element)) {
                    continue;
                }

                return FunctionalResult.NOT_MATCHED;
            }

            // verify result using registered verifiers
            if (!verify(results, synchronizedSource, element, result)) {
                // restore index if element is optional
                if (restoreIfOptional(synchronizedSource, element)) {
                    continue;
                }

                return FunctionalResult.NOT_MATCHED;
            }

            for (Function<?, ?> mapper : element.getMappers()) {
                result = mapper.apply(ObjectUtils.cast(result));
            }

            if (result instanceof FunctionalResult) {
                results.addAll(((FunctionalResult) result).results);
            }

            if (!element.getId().equals(StringUtils.EMPTY)) {
                results.add(new Pair<>(element.getId(), result));
            }
        }

        return new FunctionalResult(stream.read(synchronizedSource.getIndex()), results);
    }

    private boolean restoreIfOptional(SynchronizedSource synchronizedSource, Element<?> element) {
        if (!element.isOptional()) {
            return false;
        }

        synchronizedSource.setIndex(synchronizedSource.getCachedIndex());
        return true;
    }

    private boolean verify(List<Pair<String, Object>> results, SynchronizedSource synchronizedSource, Element<?> element, Object result) {
        for (Verifier<?> verifier : element.getVerifiers()) {
            if (!verifier.verify(results, synchronizedSource, ObjectUtils.cast(result))) {
                return false;
            }
        }

        return true;
    }

}
