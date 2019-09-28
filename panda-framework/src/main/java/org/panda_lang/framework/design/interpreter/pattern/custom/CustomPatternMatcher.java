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

package org.panda_lang.framework.design.interpreter.pattern.custom;

import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class CustomPatternMatcher {

    private final CustomPattern pattern;

    CustomPatternMatcher(CustomPattern pattern) {
        this.pattern = pattern;
    }

    public Result match(SourceStream source) {
        Map<String, Object> results = new HashMap<>();
        SynchronizedSource synchronizedSource = new SynchronizedSource(source.toSnippet());

        for (CustomPatternElement element : pattern.getElements()) {
            if (!synchronizedSource.hasNext()) {
                if (element.isOptional()) {
                    continue;
                }

                return Result.NOT_MATCHED;
            }

            synchronizedSource.cacheIndex();
            Object result = element.getReader().read(synchronizedSource, synchronizedSource.next());

            // result may be null if element is optional
            if (result == null) {
                // restore index if element is optional
                if (restoreIfOptional(synchronizedSource, element)) {
                    continue;
                }

                return Result.NOT_MATCHED;
            }

            // verify result using registered verifiers
            if (!verify(synchronizedSource, element, result)) {
                return Result.NOT_MATCHED;
            }

            for (Function mapper : element.getMappers()) {
                //noinspection unchecked
                result = mapper.apply(result);
            }

            // save result
            results.put(element.getId(), result);
        }

        return new Result(source.read(synchronizedSource.getIndex()), results);
    }

    private boolean next(Map<String, Object> results, SynchronizedSource synchronizedSource, TokenRepresentation next) {
        for (CustomPatternElement element : pattern.getElements()) {
            synchronizedSource.cacheIndex();
            Object result = element.getReader().read(synchronizedSource, next);

            // result may be null if element is optional
            if (result == null) {
                // restore index if element is optional
                if (restoreIfOptional(synchronizedSource, element)) {
                    continue;
                }

                return false;
            }

            // verify result using registered verifiers
            if (!verify(synchronizedSource, element, result)) {
                return false;
            }

            // save result
            results.put(element.getId(), result.toString());
            return true;
        }

        return false;
    }

    private boolean restoreIfOptional(SynchronizedSource synchronizedSource, CustomPatternElement element) {
        if (!element.isOptional()) {
            return false;
        }

        synchronizedSource.setIndex(synchronizedSource.getCachedIndex());
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean verify(SynchronizedSource synchronizedSource, CustomPatternElement element, Object result) {
        for (CustomVerify verifier : element.getVerifiers()) {
            if (!verifier.verify(result)) {
                return restoreIfOptional(synchronizedSource, element);
            }
        }

        return true;
    }

}
