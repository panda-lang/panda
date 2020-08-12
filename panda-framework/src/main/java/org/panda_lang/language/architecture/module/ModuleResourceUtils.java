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

package org.panda_lang.language.architecture.module;

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Collection;

final class ModuleResourceUtils {

    private ModuleResourceUtils() { }

    public static Option<Type> forClass(Collection<? extends ModuleResource> resources, Class<?> associatedClass) {
        return PandaStream.of(resources)
                .flatMap(parent -> parent.forClass(associatedClass))
                .head();
    }

    public static Option<Type> forName(Collection<? extends ModuleResource> resources, CharSequence name) {
        return PandaStream.of(resources)
                .flatMap(parent -> parent.forName(name))
                .head();
    }

}
