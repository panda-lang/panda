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

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CustomPattern {

    private final List<? extends CustomPatternElement> elements;

    private CustomPattern(List<? extends CustomPatternElement> elements) {
        this.elements = elements;
    }

    public Result match(Snippet source) {
        return match(new PandaSourceStream(source));
    }

    public Result match(SourceStream source) {
        CustomPatternMatcher matcher = new CustomPatternMatcher(this);
        return matcher.match(source);
    }

    protected List<? extends CustomPatternElement> getElements() {
        return elements;
    }

    public static CustomPattern of(Buildable... elements) {
        return new CustomPattern(Arrays.stream(elements)
                .map(Buildable::build)
                .collect(Collectors.toList()));
    }

}
