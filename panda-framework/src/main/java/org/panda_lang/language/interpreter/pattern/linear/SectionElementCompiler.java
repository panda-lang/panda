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

package org.panda_lang.language.interpreter.pattern.linear;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.resource.syntax.separator.Separator;

import java.util.Collection;
import java.util.stream.Collectors;

final class SectionElementCompiler implements LinearPatternElementCompiler {

    private Collection<Separator> separators;
    private Separator handle;

    @Override
    public void initialize(LinearPatternCompiler compiler) {
        this.separators = compiler.syntax.getSeparators().stream()
                .filter(Separator::hasOpposite)
                .collect(Collectors.toList());
    }

    @Override
    public boolean handle(LinearPatternCompiler compiler, @Nullable String identifier, String content, boolean optional) {
        this.handle = null;

        if (content.length() != 3 || content.charAt(1) != '~') {
            return false;
        }

        String left = Character.toString(content.charAt(0));
        String right = Character.toString(content.charAt(2));

        for (Separator separator : separators) {
            if (separator.getValue().equals(left) && separator.getOpposite().getValue().equals(right)) {
                this.handle = separator;
                return true;
            }
        }

        return false;
    }

    @Override
    public LinearPatternElement compile(LinearPatternCompiler compiler, @Nullable String identifier, String content, boolean optional) {
        return new SectionElement(handle, content, identifier, optional);
    }

}
