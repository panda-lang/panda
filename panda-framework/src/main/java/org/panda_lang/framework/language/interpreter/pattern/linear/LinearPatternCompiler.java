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

package org.panda_lang.framework.language.interpreter.pattern.linear;

import org.panda_lang.framework.design.resource.Syntax;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Collection;
import java.util.List;

final class LinearPatternCompiler {

    protected final Syntax syntax;
    protected final Collection<LinearPatternElementCompiler> elementCompilers;

    public LinearPatternCompiler(Syntax syntax, Collection<LinearPatternElementCompiler> compilers) {
        this.syntax = syntax;
        this.elementCompilers = compilers;
    }

    protected void initialize() {
        for (LinearPatternElementCompiler compiler : elementCompilers) {
            compiler.initialize(this);
        }
    }

    protected LinearPattern compile(String pattern) {
        LinearPatternCompilerWorker worker = new LinearPatternCompilerWorker(this, pattern);
        Option<List<LinearPatternElement>> elements = worker.compile();

        if (!elements.isPresent()) {
            throw new LinearPatternException("Cannot compile the pattern");
        }

        return new LinearPattern(elements.get());
    }

}
