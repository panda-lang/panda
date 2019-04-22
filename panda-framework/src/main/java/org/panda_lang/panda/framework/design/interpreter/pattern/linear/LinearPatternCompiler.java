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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class LinearPatternCompiler {

    protected LinearPattern compile(String pattern) {
        LinearPatternCompilerWorker worker = new LinearPatternCompilerWorker(pattern);
        Optional<List<LinearPatternElement>> elements = worker.compile();

        if (!elements.isPresent()) {
            throw new LinearPatternException("Cannot compile the pattern");
        }

        return new LinearPattern(elements.get());
    }

    private static class LinearPatternCompilerWorker {

        private final String pattern;

        private LinearPatternCompilerWorker(String pattern) {
            this.pattern = pattern;
        }

        private Optional<List<LinearPatternElement>> compile() {
            String[] elements = pattern.split(" ");
            List<LinearPatternElement> compiled = new ArrayList<>(elements.length);

            for (String element : elements) {
                LinearPatternElement compiledElement = compileElement(element);

                if (compiledElement == null) {
                    return Optional.empty();
                }

                compiled.add(compiledElement);
            }

            return Optional.of(compiled);
        }

        private @Nullable LinearPatternElement compileElement(String element) {
            if (StringUtils.isEmpty(element)) {
                return null;
            }

            String[] data = StringUtils.split(element, ":");
            String identifier = null;
            String content;

            if (data.length == 1) {
                content = data[0];
            }
            else {
                identifier = data[0];
                content = data[1];
            }

            if (content.startsWith("*")) {
                return new WildcardLinearPatternElement(identifier);
            }

            return new UnitLinearPatternElement(identifier, content);
        }

    }

}
