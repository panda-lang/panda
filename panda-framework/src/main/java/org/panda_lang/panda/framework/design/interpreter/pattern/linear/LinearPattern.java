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

import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.List;

public class LinearPattern {

    private static final LinearPatternCompiler COMPILER = new LinearPatternCompiler();

    private final List<LinearPatternElement> element;

    LinearPattern(List<LinearPatternElement> element) {
        this.element = element;
    }

    public LinearPatternResult match(Snippet source) {
        return match(new PandaSourceStream(source));
    }

    public LinearPatternResult match(SourceStream source) {
        return new LinearPatternMatcher(this, source).match();
    }

    protected List<LinearPatternElement> getElements() {
        return element;
    }

    public static LinearPattern compile(String pattern) {
        return COMPILER.compile(pattern);
    }

}
