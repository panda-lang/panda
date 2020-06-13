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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.List;

final class LinearPatternCompilerWorker {

    private final LinearPatternCompiler compiler;
    private final String pattern;

    LinearPatternCompilerWorker(LinearPatternCompiler compiler, String pattern) {
        this.compiler = compiler;
        this.pattern = pattern;
    }

    Option<List<LinearPatternElement>> compile() {
        String[] elements = StringUtils.split(pattern, " ");
        List<LinearPatternElement> compiled = new ArrayList<>(elements.length);

        for (String element : elements) {
            LinearPatternElement compiledElement = compileElement(element);

            if (compiledElement == null) {
                return Option.none();
            }

            compiled.add(compiledElement);
        }

        return Option.of(compiled);
    }

    private @Nullable LinearPatternElement compileElement(String element) {
        if (StringUtils.isEmpty(element)) {
            return null;
        }

        String[] data = StringUtils.split(element, ":");
        String identifier = data.length == 1 ? null : data[0];
        String content = identifier != null ? data[1] : data[0];

        boolean optional = identifier != null && identifier.startsWith("&");

        if (optional) {
            identifier = identifier.substring(1);
        }

        for (LinearPatternElementCompiler elementCompiler : compiler.elementCompilers) {
            if (elementCompiler.handle(compiler, identifier, content, optional)) {
                return elementCompiler.compile(compiler, identifier, content, optional);
            }
        }

        return new UnitElement(identifier, content, optional);
    }

}
