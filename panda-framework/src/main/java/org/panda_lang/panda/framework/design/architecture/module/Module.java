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

package org.panda_lang.panda.framework.design.architecture.module;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Optional;

public interface Module {

    ClassPrototypeReference add(ClassPrototypeReference prototype);

    default boolean hasClass(Class<?> clazz) {
        return this.hasClass(clazz.getSimpleName());
    }

    default boolean hasClass(String className) {
        return get(className).isPresent();
    }

    default Optional<ClassPrototypeReference> getAssociatedWith(Class<?> clazz) {
        return StreamUtils.findFirst(getReferences(), reference -> reference.getAssociatedClass() == clazz);
    }

    default Optional<ClassPrototypeReference> get(String className) {
        return StreamUtils.findFirst(getReferences(), reference -> className.equals(reference.getClassName()));
    }

    default int getAmountOfUsedPrototypes() {
        return StreamUtils.count(getReferences(), ClassPrototypeReference::isInitialized);
    }

    int getAmountOfReferences();

    Iterable<ClassPrototypeReference> getReferences();

    String getName();

}
