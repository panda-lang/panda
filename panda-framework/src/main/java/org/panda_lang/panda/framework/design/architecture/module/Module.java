/*
 * Copyright (c) 2015-2018 Dzikoysk
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
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;

import java.util.Collection;

public interface Module {

    ClassPrototype add(ClassPrototype prototype);

    default boolean hasClass(Class<?> clazz) {
        return this.hasClass(clazz.getSimpleName());
    }

    default boolean hasClass(String className) {
        for (ClassPrototype prototype : this.getPrototypes()) {
            if (prototype.getClassName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    default @Nullable ClassPrototype get(Class<?> clazz) {
        return this.get(clazz.getSimpleName());
    }

    default @Nullable ClassPrototype get(String className) {
        for (ClassPrototype prototype : this.getPrototypes()) {
            if (prototype.isClassOf(className)) {
                return prototype;
            }
        }

        return null;
    }

    Collection<? extends ClassPrototype> getPrototypes();

    String getName();

    default int getAmountOfPrototypes() {
        return this.getPrototypes().size();
    }

}
