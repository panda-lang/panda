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

package org.panda_lang.framework.language.interpreter.pattern.linear;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;

final class SectionElement extends LinearPatternElement {

    private final Separator separator;

    public SectionElement(Separator separator, String value, @Nullable String identifier, boolean optional) {
        super(value, identifier, optional);
        this.separator = separator;
    }

    @Override
    boolean isSection() {
        return true;
    }

    public Separator getSeparator() {
        return separator;
    }

}
