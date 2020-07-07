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
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class FunctionalPattern {

    private final List<? extends Element<?>> elements;

    FunctionalPattern(List<? extends Element<?>> elements) {
        this.elements = elements;
    }

    public FunctionalResult match(Snippet source) {
        return match(source, new PandaSourceStream(source));
    }

    public FunctionalResult match(Snippet source, @Nullable PatternData data) {
        return match(source, new PandaSourceStream(source), data);
    }

    public FunctionalResult match(Snippet source, SourceStream stream) {
        return match(source, stream, PatternData.empty());
    }

    public FunctionalResult match(Snippet source, SourceStream stream, @Nullable PatternData data) {
        FunctionalPatternMatcher matcher = new FunctionalPatternMatcher(this, data);
        return matcher.match(source, stream);
    }

    protected List<? extends Element<?>> getElements() {
        return elements;
    }

    public static FunctionalPattern of(Buildable<?>... elements) {
        return of(Arrays.asList(elements));
    }

    protected static FunctionalPattern of(List<Buildable<?>> elements) {
        return new FunctionalPattern(elements.stream()
                .map(Buildable::build)
                .collect(Collectors.toList()));
    }

    public static FunctionalPatternBuilder<?, ?> builder() {
        return new FunctionalPatternBuilder<>();
    }

}
