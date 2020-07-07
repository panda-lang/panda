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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.pattern.custom.Buildable;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomReader;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

public final class SubPatternElement extends CustomPatternElementBuilder<Result, SubPatternElement> {

    private SubPatternElement(String id) {
        super(id);
    }

    public SubPatternElement of(Buildable<?>... elements) {
        return of(CustomPattern.of(elements));
    }

    public SubPatternElement of(CustomPattern pattern) {
        super.custom(createReader(pattern));
        return this;
    }

    protected static CustomReader<Result> createReader(CustomPattern pattern) {
        return (data, source) -> {
            Snippet availableSource = source.getAvailableSource();
            SourceStream stream = new PandaSourceStream(availableSource);
            Result result = pattern.match(availableSource, stream, data);

            if (!result.isMatched()) {
                return null;
            }

            source.next(stream.getReadLength());
            return result;
        };
    }

    public static SubPatternElement create(String id) {
        return new SubPatternElement(id);
    }

}
