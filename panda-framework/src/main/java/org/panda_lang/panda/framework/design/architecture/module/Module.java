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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;

import java.util.Collection;

public interface Module {

    ClassPrototypeReference add(ClassPrototypeReference prototype);

    default boolean hasClass(Class<?> clazz) {
        return this.hasClass(clazz.getSimpleName());
    }

    default boolean hasClass(String className) {
        for (ClassPrototypeReference reference : this.getReferences()) {
            if (reference.getClassName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    default @Nullable ClassPrototypeReference getAssociatedWith(Class<?> clazz) {
        for (ClassPrototypeReference reference : getReferences()) {
            if (reference.getAssociatedClass() == clazz) {
                return reference;
            }
        }

        return null;
    }

    default @Nullable ClassPrototypeReference get(String className) {
        for (ClassPrototypeReference reference : getReferences()) {
            if (className.equals(reference.getClassName())) {
                return reference;
            }
        }

        return null;
    }

    default int getAmountOfPrototypes() {
        return this.getReferences().size();
    }

    Collection<? extends ClassPrototypeReference> getReferences();

    String getName();

}
