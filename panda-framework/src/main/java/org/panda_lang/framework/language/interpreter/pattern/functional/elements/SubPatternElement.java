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

package org.panda_lang.framework.language.interpreter.pattern.functional.elements;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.pattern.functional.Buildable;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalPattern;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalPatternBuilder;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.pattern.functional.FunctionalResult;
import org.panda_lang.framework.language.interpreter.pattern.functional.Reader;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

import java.util.function.Function;

public final class SubPatternElement extends FunctionalPatternElementBuilder<FunctionalResult, SubPatternElement> {

    private SubPatternElement(String id) {
        super(id);
    }

    public SubPatternElement of(Buildable<?>... elements) {
        return of(FunctionalPattern.of(elements));
    }

    public SubPatternElement of(FunctionalPattern pattern) {
        super.reader(createReader(pattern));
        return this;
    }

    protected static Reader<FunctionalResult> createReader(FunctionalPattern pattern) {
        return (data, source) -> {
            Snippet availableSource = source.getAvailableSource();
            SourceStream stream = new PandaSourceStream(availableSource);
            FunctionalResult functionalResult = pattern.match(availableSource, stream, data);

            if (!functionalResult.isMatched()) {
                return null;
            }

            source.next(stream.getReadLength());
            return functionalResult;
        };
    }

    public static SubPatternElement create(String id) {
        return new SubPatternElement(id);
    }

    public static SubPatternElement create(String id, Function<FunctionalPatternBuilder<?, ?>, FunctionalPatternBuilder<?, ?>> function) {
        return create(id).of(function.apply(FunctionalPattern.builder()).build());
    }

}
