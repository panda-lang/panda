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

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LinearPatternMatcher {

    private final LinearPattern pattern;
    private final SourceStream source;

    LinearPatternMatcher(LinearPattern pattern, SourceStream source) {
        this.pattern = pattern;
        this.source = source;
    }

    LinearPatternResult match() {
        DiffusedSource content = new DiffusedSource(source.toSnippet());
        List<String> identifiers = new ArrayList<>();
        Map<String, Object> wildcards = new HashMap<>();

        for (LinearPatternElement element : pattern.getElements()) {
            if (!match(content, identifiers, wildcards, element)) {
                return new LinearPatternResult();
            }
        }

        source.read(content.getIndex());
        return new LinearPatternResult(identifiers, wildcards);
    }

    private boolean match(DiffusedSource content, List<String> identifiers, Map<String, Object> wildcards, LinearPatternElement element) {
        if (!content.hasNext()) {
            return false;
        }

        TokenRepresentation representation = content.next();

        if (element.isUnit()) {
            if (element.getIdentifier().isPresent()) {
                identifiers.add(element.getIdentifier().get());
            }

            return representation.getTokenValue().equals(element.getValue());
        }

        if (element.isWildcard()) {
            if (!element.getIdentifier().isPresent()) {
                return true;
            }

            identifiers.add(element.getIdentifier().get());
            wildcards.put(element.getIdentifier().get(), representation);
            return true;
        }

        return false;
    }

}
